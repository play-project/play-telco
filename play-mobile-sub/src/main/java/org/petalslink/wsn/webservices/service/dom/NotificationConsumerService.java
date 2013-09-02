package org.petalslink.wsn.webservices.service.dom;

import java.util.Date;

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
import com.orange.play.gcmAPI.Registration;

@WebServiceProvider(wsdlLocation = "WS-NotificationConsumer.wsdl", serviceName = "NotificationConsumerService", portName = "NotificationConsumerPort", targetNamespace = "http://docs.oasis-open.org/wsn/bw-2")
@ServiceMode(value = javax.xml.ws.Service.Mode.MESSAGE)
public class NotificationConsumerService implements Provider<SOAPMessage> {

	public static final String WSNT_UNREGISTER = "Unregister";
    public static final String WSNT_REGISTER = "Register";
    public static final String WSNT_NOTIFY = "Notify";
    
    private static final String WSNT_PHONE_NUMBER = "phoneNumber";
    private static final String WSNT_PHONE_REGID = "registrationID";
    
    private final Registration gcmRegIds = Registration.getInstance();

    static {
        Wsnb4ServUtils.initModelFactories(new WsrfbfModelFactoryImpl(),
                new WsrfrModelFactoryImpl(), new WsrfrlModelFactoryImpl(),
                new WsrfrpModelFactoryImpl(), new WstopModelFactoryImpl(),
                new WsnbModelFactoryImpl());
    }

    @Resource
    WebServiceContext wsContext;

    
    
    @Override
	public SOAPMessage invoke(SOAPMessage request) {

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
                
                NodeList registrationIDNL = request.getSOAPBody().getElementsByTagName(WSNT_PHONE_REGID);
                NodeList phoneNumberNL = request.getSOAPBody().getElementsByTagName(WSNT_PHONE_NUMBER);
                
                if (registrationIDNL.getLength() > 0 && phoneNumberNL.getLength() > 0) {
                    String registrationId =  registrationIDNL.item(0).getTextContent();
                    String phoneNumber = phoneNumberNL.item(0).getTextContent();
                    
                    System.out.format("\t%s = %s\n", WSNT_PHONE_REGID, registrationId);
                    System.out.format("\t%s = %s\n", WSNT_PHONE_NUMBER, phoneNumber);
                    
                    if (registrationId != null && phoneNumber != null) {
                        System.err.println("\tadding to phoneNumberToRegistrationId...");
                        gcmRegIds.addPhoneNumberMapping(registrationId, phoneNumber);
                        System.out.format("\tadded => phoneNumberToRegistrationId = %s\n", phoneNumber);
                        
                        NodeList topicsNL = request.getSOAPBody().getElementsByTagName("topics");
                       	for (int i = 0; i < topicsNL.getLength(); i++) {
                       		String topic = topicsNL.item(i).getTextContent().trim();
                       		if (!topic.isEmpty()) {
                       			gcmRegIds.addTopicMapping(registrationId, topic);
                       		}
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
                
                NodeList phoneNumberNL = request.getSOAPBody().getElementsByTagName(WSNT_PHONE_NUMBER);
                NodeList registrationIdNL = request.getSOAPBody().getElementsByTagName(WSNT_PHONE_REGID);

                if (phoneNumberNL != null && phoneNumberNL.getLength() > 0) {
                    String phoneNumber = phoneNumberNL.item(0).getTextContent();
                    
                    System.out.format("\tphoneNumber = %s\n", phoneNumber);

                    if (phoneNumber != null) {
                       System.err.println("\tremoving from phoneNumberToRegistrationId...");
                       String oldId = gcmRegIds.getRegistrationIdForPhoneNumber(phoneNumber);
                       gcmRegIds.removeRegistrationId(oldId);
                    }
                }
                
                if (registrationIdNL != null && registrationIdNL.getLength() > 0) {
                    String registrationId = registrationIdNL.item(0).getTextContent();
                    
                    System.out.format("\tregistrationId = %s\n", registrationId);

                    if (registrationId != null) {
                       System.err.println("\tremoving registrationId...");
                       gcmRegIds.removeRegistrationId(registrationId);
                    }
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

    private void handleFault(Exception e) {
        System.out.format("%s: FAULT: %s\n", new Date().toString(), e.getMessage());
        e.printStackTrace();
    }

    private void handleFault(String message) {
        System.out.format("%s: FAULT: %s\n", new Date().toString(), message);
    }

}
