<%@ WebHandler  Language="C#"  Class="getContent" %>
/**
 * Created by visual studio 2010
 * User: xuheng
 * Date: 12-3-6
 * Time: 下午21:23
 * To get the value of editor and output the value .
 */
using System;
using System.Web;

namespace Java21.Net
{
    public class getContent : IHttpHandler
    {
        public void ProcessRequest(HttpContext context)
        {
            context.Response.ContentType = "text/html";

            //获取数据
            string content = context.Request.Form["javaEditor"];


            //存入数据库或者其他操作
            //-------------

            //显示
            context.Response.Write("<script src='../script/jquery.min.js'></script>");
            context.Response.Write("<script src='../script/mathquill/mathquill.js'></script>");
            context.Response.Write("<link rel='stylesheet' href='../script/mathquill/mathquill.css'/>");
            context.Response.Write("<div class='content'>" + content + "</div>");
        }

        public bool IsReusable
        {
            get
            {
                return false;
            }
        }
    }
}