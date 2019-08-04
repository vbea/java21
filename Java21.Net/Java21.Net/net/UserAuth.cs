using Java21.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace Java21.NET
{
    public class UserAuth : IAuthorizationFilter
    {
        const string mLogin = @"<a href='/Users/Login'>登录</a>|<a href='/Users/SignIn'>注册</a>";
        const string mUser = "<figure><a id='id_user' href='javascript:void(0);'><div class='user_log'><img src='{0}' /></div><div class='user_div'>{1}</div></a></figure>";
        public static string actionName{ get; set; }
        public static string lastAction { get; set;}
        public static string resultAction { get; set; }
        public static string controller { get; set; }
        public static string lastcontroller { get; set; }
        public static string recontroller { get; set; }
        private static bool first = true;

        private bool isLogin(HttpContextBase context)
        {
            UserInfo info = (UserInfo)context.Session["LoginUser"];
            if (info != null)
            {
                
            }
            return false;
        }

        public static void Logout()
        {
            first = true;
        }

        void IAuthorizationFilter.OnAuthorization(AuthorizationContext context)
        {
            lastAction = actionName;
            lastcontroller = controller;
            actionName = context.ActionDescriptor.ActionName;
            controller = context.ActionDescriptor.ControllerDescriptor.ControllerName;
            if (actionName.Equals("Login") || actionName.Equals("SignIn"))
            {
                if (first)
                {
                    resultAction = lastAction;
                    recontroller = lastcontroller;
                    first = !first;
                }
                return;
            }
            else
                first = true;
            UserInfo info = (UserInfo)context.HttpContext.Session["LoginUser"];
            if (info != null)
                context.Controller.ViewData["Div_User"] = string.Format(mUser, (info.HeadImg == null?"/images/head.jpg":info.HeadImg), info.NickName);
            else
                context.Controller.ViewData["Div_User"] = mLogin;
            
        }
    }
}