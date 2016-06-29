

/**
 * SendSmsService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package org.csapi.www.wsdl.parlayx.sms.send.v2_2.service;

    /*
     *  SendSmsService java interface
     */

    public interface SendSmsService {
          

        /**
          * Auto generated method signature
          * 
                    * @param sendSmsLogo0
                
             * @throws org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.PolicyException : 
             * @throws org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.ServiceException : 
         */

         
                     public org.csapi.www.schema.parlayx.sms.send.v2_2.local.SendSmsLogoResponseE sendSmsLogo(

                        org.csapi.www.schema.parlayx.sms.send.v2_2.local.SendSmsLogoE sendSmsLogo0)
                        throws java.rmi.RemoteException
             
          ,org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.PolicyException
          ,org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.ServiceException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param sendSmsLogo0
            
          */
        public void startsendSmsLogo(

            org.csapi.www.schema.parlayx.sms.send.v2_2.local.SendSmsLogoE sendSmsLogo0,

            final org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.SendSmsServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param sendSmsRingtone2
                
             * @throws org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.PolicyException : 
             * @throws org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.ServiceException : 
         */

         
                     public org.csapi.www.schema.parlayx.sms.send.v2_2.local.SendSmsRingtoneResponseE sendSmsRingtone(

                        org.csapi.www.schema.parlayx.sms.send.v2_2.local.SendSmsRingtoneE sendSmsRingtone2)
                        throws java.rmi.RemoteException
             
          ,org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.PolicyException
          ,org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.ServiceException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param sendSmsRingtone2
            
          */
        public void startsendSmsRingtone(

            org.csapi.www.schema.parlayx.sms.send.v2_2.local.SendSmsRingtoneE sendSmsRingtone2,

            final org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.SendSmsServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param getSmsDeliveryStatus4
                
             * @throws org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.PolicyException : 
             * @throws org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.ServiceException : 
         */

         
                     public org.csapi.www.schema.parlayx.sms.send.v2_2.local.GetSmsDeliveryStatusResponseE getSmsDeliveryStatus(

                        org.csapi.www.schema.parlayx.sms.send.v2_2.local.GetSmsDeliveryStatusE getSmsDeliveryStatus4)
                        throws java.rmi.RemoteException
             
          ,org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.PolicyException
          ,org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.ServiceException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param getSmsDeliveryStatus4
            
          */
        public void startgetSmsDeliveryStatus(

            org.csapi.www.schema.parlayx.sms.send.v2_2.local.GetSmsDeliveryStatusE getSmsDeliveryStatus4,

            final org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.SendSmsServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param sendSms6
                
             * @throws org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.PolicyException : 
             * @throws org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.ServiceException : 
         */

         
                     public org.csapi.www.schema.parlayx.sms.send.v2_2.local.SendSmsResponseE sendSms(

                        org.csapi.www.schema.parlayx.sms.send.v2_2.local.SendSmsE sendSms6)
                        throws java.rmi.RemoteException
             
          ,org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.PolicyException
          ,org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.ServiceException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param sendSms6
            
          */
        public void startsendSms(

            org.csapi.www.schema.parlayx.sms.send.v2_2.local.SendSmsE sendSms6,

            final org.csapi.www.wsdl.parlayx.sms.send.v2_2.service.SendSmsServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    