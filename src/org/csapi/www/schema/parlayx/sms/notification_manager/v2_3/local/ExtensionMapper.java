
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:34:40 IST)
 */

        
            package org.csapi.www.schema.parlayx.sms.notification_manager.v2_3.local;
        
            /**
            *  ExtensionMapper class
            */
            @SuppressWarnings({"unchecked","unused"})
        
        public  class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "http://www.csapi.org/schema/parlayx/common/v2_1".equals(namespaceURI) &&
                  "ServiceException".equals(typeName)){
                   
                            return  org.csapi.www.schema.parlayx.common.v2_1.ServiceException.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.csapi.org/schema/parlayx/common/v2_1".equals(namespaceURI) &&
                  "PolicyException".equals(typeName)){
                   
                            return  org.csapi.www.schema.parlayx.common.v2_1.PolicyException.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.csapi.org/schema/parlayx/sms/notification_manager/v2_3/local".equals(namespaceURI) &&
                  "startSmsNotification".equals(typeName)){
                   
                            return  org.csapi.www.schema.parlayx.sms.notification_manager.v2_3.local.StartSmsNotification.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.csapi.org/schema/parlayx/sms/notification_manager/v2_3/local".equals(namespaceURI) &&
                  "startSmsNotificationResponse".equals(typeName)){
                   
                            return  org.csapi.www.schema.parlayx.sms.notification_manager.v2_3.local.StartSmsNotificationResponse.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.csapi.org/schema/parlayx/common/v2_1".equals(namespaceURI) &&
                  "SimpleReference".equals(typeName)){
                   
                            return  org.csapi.www.schema.parlayx.common.v2_1.SimpleReference.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.csapi.org/schema/parlayx/sms/notification_manager/v2_3/local".equals(namespaceURI) &&
                  "stopSmsNotification".equals(typeName)){
                   
                            return  org.csapi.www.schema.parlayx.sms.notification_manager.v2_3.local.StopSmsNotification.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.csapi.org/schema/parlayx/sms/notification_manager/v2_3/local".equals(namespaceURI) &&
                  "stopSmsNotificationResponse".equals(typeName)){
                   
                            return  org.csapi.www.schema.parlayx.sms.notification_manager.v2_3.local.StopSmsNotificationResponse.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    