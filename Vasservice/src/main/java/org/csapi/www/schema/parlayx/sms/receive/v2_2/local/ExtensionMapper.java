
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:34:40 IST)
 */

        
            package org.csapi.www.schema.parlayx.sms.receive.v2_2.local;
        
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
                  "http://www.csapi.org/schema/parlayx/sms/v2_2".equals(namespaceURI) &&
                  "SmsMessage".equals(typeName)){
                   
                            return  org.csapi.www.schema.parlayx.sms.v2_2.SmsMessage.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.csapi.org/schema/parlayx/sms/receive/v2_2/local".equals(namespaceURI) &&
                  "getReceivedSms".equals(typeName)){
                   
                            return  org.csapi.www.schema.parlayx.sms.receive.v2_2.local.GetReceivedSms.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://www.csapi.org/schema/parlayx/sms/receive/v2_2/local".equals(namespaceURI) &&
                  "getReceivedSmsResponse".equals(typeName)){
                   
                            return  org.csapi.www.schema.parlayx.sms.receive.v2_2.local.GetReceivedSmsResponse.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    