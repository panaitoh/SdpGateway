

/**
 * SmsNotificationManagerService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service;

    /*
     *  SmsNotificationManagerService java interface
     */

    public interface SmsNotificationManagerService {
          

        /**
          * Auto generated method signature
          * 
                    * @param stopSmsNotification0
                
             * @throws org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.PolicyException : 
             * @throws org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.ServiceException : 
         */

         
                     public org.csapi.www.schema.parlayx.sms.notification_manager.v2_3.local.StopSmsNotificationResponseE stopSmsNotification(

                        org.csapi.www.schema.parlayx.sms.notification_manager.v2_3.local.StopSmsNotificationE stopSmsNotification0)
                        throws java.rmi.RemoteException
             
          ,org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.PolicyException
          ,org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.ServiceException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param stopSmsNotification0
            
          */
        public void startstopSmsNotification(

            org.csapi.www.schema.parlayx.sms.notification_manager.v2_3.local.StopSmsNotificationE stopSmsNotification0,

            final org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.SmsNotificationManagerServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param startSmsNotification2
                
             * @throws org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.PolicyException : 
             * @throws org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.ServiceException : 
         */

         
                     public org.csapi.www.schema.parlayx.sms.notification_manager.v2_3.local.StartSmsNotificationResponseE startSmsNotification(

                        org.csapi.www.schema.parlayx.sms.notification_manager.v2_3.local.StartSmsNotificationE startSmsNotification2)
                        throws java.rmi.RemoteException
             
          ,org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.PolicyException
          ,org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.ServiceException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param startSmsNotification2
            
          */
        public void startstartSmsNotification(

            org.csapi.www.schema.parlayx.sms.notification_manager.v2_3.local.StartSmsNotificationE startSmsNotification2,

            final org.csapi.www.wsdl.parlayx.sms.notification_manager.v2_3.service.SmsNotificationManagerServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    