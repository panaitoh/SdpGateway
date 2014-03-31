

/**
 * ReceiveSmsService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package org.csapi.www.wsdl.parlayx.sms.receive.v2_2.service;

    /*
     *  ReceiveSmsService java interface
     */

    public interface ReceiveSmsService {
          

        /**
          * Auto generated method signature
          * 
                    * @param getReceivedSms0
                
             * @throws org.csapi.www.wsdl.parlayx.sms.receive.v2_2.service.PolicyException : 
             * @throws org.csapi.www.wsdl.parlayx.sms.receive.v2_2.service.ServiceException : 
         */

         
                     public org.csapi.www.schema.parlayx.sms.receive.v2_2.local.GetReceivedSmsResponseE getReceivedSms(

                        org.csapi.www.schema.parlayx.sms.receive.v2_2.local.GetReceivedSmsE getReceivedSms0)
                        throws java.rmi.RemoteException
             
          ,org.csapi.www.wsdl.parlayx.sms.receive.v2_2.service.PolicyException
          ,org.csapi.www.wsdl.parlayx.sms.receive.v2_2.service.ServiceException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getReceivedSms0
            
          */
        public void startgetReceivedSms(

            org.csapi.www.schema.parlayx.sms.receive.v2_2.local.GetReceivedSmsE getReceivedSms0,

            final org.csapi.www.wsdl.parlayx.sms.receive.v2_2.service.ReceiveSmsServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    