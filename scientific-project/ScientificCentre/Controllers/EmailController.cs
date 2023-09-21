using ScientificCentreData;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Mail;
using System.Web;

namespace ScientificCentre.Controllers
{
    public class EmailController
    {

        public EmailController()
        {

        }
        public void SendMail(string reciverEmail, string message, string subject)
        {
            SmtpClient client = new SmtpClient
            {
                Port = 587,
                Host = "smtp.gmail.com",
                EnableSsl = true,
                DeliveryMethod = SmtpDeliveryMethod.Network,
                UseDefaultCredentials = false,
                Credentials = new NetworkCredential("scientificcentrepage@gmail.com", "Kk6395539") 
            };
            MailMessage mailMessage = new MailMessage
            {
                From = new MailAddress("scientificcentrepage@gmail.com"),
                IsBodyHtml = true
            };
            mailMessage.To.Add(reciverEmail);
            mailMessage.Subject = subject;
            mailMessage.Body = message;
            client.Send(mailMessage);
        }
    }
}