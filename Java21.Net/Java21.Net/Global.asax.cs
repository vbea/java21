using Java21.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Security;
using System.Web.SessionState;
using ToolDAL;

namespace Java21.Net
{
    public class Global : System.Web.HttpApplication
    {

        protected void Application_Start(object sender, EventArgs e)
        {

        }

        protected void Session_Start(object sender, EventArgs e)
        {

        }

        protected void Application_BeginRequest(object sender, EventArgs e)
        {

        }

        protected void Application_AuthenticateRequest(object sender, EventArgs e)
        {

        }

        protected void Application_Error(object sender, EventArgs e)
        {
            Exception ex = Server.GetLastError();
            if (null != ex)
            {
                if (null != ex.InnerException)
                {
                    ex = ex.InnerException;
                }
                
                Log.e("Website Error: " + ex.Message);
                Log.e(ex.ToString());
            }
            HttpContext.Current.Response.Clear();
            HttpContext.Current.Response.Redirect("404.html");
            HttpContext.Current.Response.Flush();
        }

        protected void Session_End(object sender, EventArgs e)
        {

        }

        protected void Application_End(object sender, EventArgs e)
        {

        }
    }
}