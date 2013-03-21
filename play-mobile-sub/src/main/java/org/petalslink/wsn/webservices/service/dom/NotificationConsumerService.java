package org.petalslink.wsn.webservices.service.dom;

import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.annotation.Resource;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Provider;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.handler.MessageContext;

import org.petalslink.dsb.saaj.utils.SOAPMessageUtils;
import org.petalslink.wsn.webservices.service.NotificationConsumerImpl;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.ebmwebsourcing.wsstar.basefaults.datatypes.impl.impl.WsrfbfModelFactoryImpl;
import com.ebmwebsourcing.wsstar.basenotification.datatypes.api.abstraction.Notify;
import com.ebmwebsourcing.wsstar.basenotification.datatypes.api.refinedabstraction.RefinedWsnbFactory;
import com.ebmwebsourcing.wsstar.basenotification.datatypes.api.utils.WsnbException;
import com.ebmwebsourcing.wsstar.basenotification.datatypes.impl.impl.WsnbModelFactoryImpl;
import com.ebmwebsourcing.wsstar.resource.datatypes.impl.impl.WsrfrModelFactoryImpl;
import com.ebmwebsourcing.wsstar.resourcelifetime.datatypes.impl.impl.WsrfrlModelFactoryImpl;
import com.ebmwebsourcing.wsstar.resourceproperties.datatypes.impl.impl.WsrfrpModelFactoryImpl;
import com.ebmwebsourcing.wsstar.topics.datatypes.impl.impl.WstopModelFactoryImpl;
import com.ebmwebsourcing.wsstar.wsnb.services.impl.util.Wsnb4ServUtils;

@WebServiceProvider(wsdlLocation = "WS-NotificationConsumer.wsdl", serviceName = "NotificationConsumerService", portName = "NotificationConsumerPort", targetNamespace = "http://docs.oasis-open.org/wsn/bw-2")
@ServiceMode(value = javax.xml.ws.Service.Mode.MESSAGE)
public class NotificationConsumerService implements Provider<SOAPMessage> {

    private static final String WSNT_UNREGISTER = "Unregister";
    private static final String WSNT_REGISTER = "Register";
    private static final String WSNT_NOTIFY = "Notify";
    
    public static HashMap<String, String> registrationIDs = null;

    static {
        Wsnb4ServUtils.initModelFactories(new WsrfbfModelFactoryImpl(),
                new WsrfrModelFactoryImpl(), new WsrfrlModelFactoryImpl(),
                new WsrfrpModelFactoryImpl(), new WstopModelFactoryImpl(),
                new WsnbModelFactoryImpl());
    }

    @Resource
    WebServiceContext wsContext;

    public SOAPMessage invoke(SOAPMessage request) {
        if (registrationIDs == null) {
            System.out.println("The registrationIDs HashMap doesn't exist => create it");
            registrationIDs = new HashMap<String, String>();
        }

        QName operation = getOperation();
        Document in = null;
        try {
            in = SOAPMessageUtils.getBodyFromMessage(request);
        } catch (SOAPException e) {
            handleFault(e);
        }

        String operationLocalPart = operation.getLocalPart();
        if (operationLocalPart.equals(WSNT_NOTIFY)) {
            try {
                System.out.println(String.format("%s: NOTIFICATION", new Date().toString()));
                
                Notify notify = RefinedWsnbFactory.getInstance().getWsnbReader().readNotify(in);
                NotificationConsumerImpl.getInstance().notify(notify);
            } catch (WsnbException e) {
                handleFault(e);
            }
        } else if (operationLocalPart.equals(WSNT_REGISTER)) {
            try {
                System.out.println(String.format("%s: REGISTER", new Date().toString()));
                
                NodeList registrationIDNL = request.getSOAPBody().getElementsByTagName("registrationID");
                NodeList phoneNumberNL = request.getSOAPBody().getElementsByTagName("phoneNumber");
                
                if (registrationIDNL != null && phoneNumberNL != null) {
                    String registrationId =  registrationIDNL.item(0).getTextContent();
                    String phoneNumber = phoneNumberNL.item(0).getTextContent();
                    
                    System.out.format("\tregistrationId = %s\n", registrationId);
                    System.out.format("\tphoneNumber = %s\n", phoneNumber);
                    
                    if (registrationId != null && phoneNumber != null) {
                        if (isPhoneNumber(phoneNumber)) {
                            System.err.println("\tadding to registrationIDs...");
                            registrationIDs.put(phoneNumber, registrationId);
                            System.out.format("\tadded => registrationIDs = %s\n", registrationIDs.toString());
                        } else {
                            handleFault("invalid phoneNumber: check your international style e.g. 336XXXXXXXX ");
                        }
                    } else {
                        handleFault("registrationID or phoneNumber content not specified");
                    }
                } else {
                    handleFault("registrationID or phoneNumber not specified");
                }
            } catch (SOAPException e) {
                handleFault(e);
            }
        } else if (operationLocalPart.equals(WSNT_UNREGISTER)) {
            try {
                System.out.println(String.format("%s: UNREGISTER", new Date().toString()));
                
                NodeList phoneNumberNL = request.getSOAPBody().getElementsByTagName("phoneNumber");

                if (phoneNumberNL != null) {
                    String phoneNumber = phoneNumberNL.item(0).getTextContent();
                    
                    System.out.format("\tphoneNumber = %s\n", phoneNumber);

                    if (phoneNumber != null) {
                        if (isPhoneNumber(phoneNumber)) {
                            if (registrationIDs.containsKey(phoneNumber)) {
                                System.err.println("\tremoving from registrationIDs...");
                                registrationIDs.remove(phoneNumber);
                                System.out.format("\tadded => registrationIDs = %s\n", registrationIDs.toString());
                            } else {
                                handleFault("no previous registration for " + phoneNumber);
                            }                            
                        } else {
                            handleFault("invalid phoneNumber: check your international style e.g. 336XXXXXXXX ");
                        }
                    } else {
                        handleFault("registrationID or phoneNumber content not specified");
                    }
                } else {
                    handleFault("registrationID or phoneNumber not specified");
                }
            } catch (SOAPException e) {
                handleFault(e);
            }
        } else {
            handleFault("invalid operation: " + operation);
        }

        // Does not return anything, must be a in-only operation!
        // the WS stack may detect the in-only operation and does not care about
        // this result (true with CXF)
        try {
            return SOAPMessageUtils.createSOAPMessageFromBodyContent(null);
        } catch (SOAPException e) {
            handleFault(e);
        }
        return null;
    }

    private QName getOperation() {
        QName result = null;
        if (wsContext != null && wsContext.getMessageContext() != null) {
            Object o = wsContext.getMessageContext().get(MessageContext.WSDL_OPERATION);
            if (o != null && o instanceof QName) {
                result = (QName) o;
            }
        }
        return result;
    }

    private boolean isPhoneNumber(String phoneNumber) {
        System.out.format("Verifying phoneNumber %s\n", phoneNumber);
        try {
            Pattern p = Pattern.compile("^[0-9]+$");
            Matcher m = p.matcher(phoneNumber);
            if (m.find()) {
                System.out.format("The phoneNumber %s is ok\n", phoneNumber);
                return true;
            }
        } catch (PatternSyntaxException e) {
            System.out.format("The phoneNumber %s is invalid: %s\n", phoneNumber, e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private void handleFault(Exception e) {
        System.out.format("%s: FAULT: %s\n", new Date().toString(), e.getMessage());
        e.printStackTrace();
    }

    private void handleFault(String message) {
        System.out.format("%s: FAULT: %s\n", new Date().toString(), message);
    }
}
