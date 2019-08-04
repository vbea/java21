using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;

namespace Database_Tools
{
    public class Common
    {
        ///// <summary>
        ///// 全局连接字符串变量
        ///// </summary>
        //public string mConn { get; set; }

        ///// <summary>
        ///// 连接状态ID值
        ///// </summary>
        //public int LinkID { get; set; }

        ///// <summary>
        ///// 临时连接字符串变量
        ///// </summary>
        //public string sConn { get; set; }

        ///// <summary>
        ///// 服务器名
        ///// </summary>
        //public string Server { get; set; }

        ///// <summary>
        ///// 数据库名
        ///// </summary>
        //public string Database { get; set; }

        //public Common(string server,string connection)
        //{
        //    this.Server = server;
        //    this.mConn = connection;
        //}

        //public Common()
        //{
        //    this.mConn = "";
        //    this.sConn = "";
        //    this.Server = "";
        //    this.LinkID = -1;
        //}

        /// <summary>
        /// 注册JS脚本
        /// </summary>
        /// <param name="page">页面</param>
        /// <param name="key">key</param>
        /// <param name="script">脚本</param>
        public static void regesterScript(Page page,string key,string script)
        {
            page.ClientScript.RegisterStartupScript(page.GetType(), key, "<script>" + script + "</script>");
        }

        /// <summary>
        /// 弹窗JS脚本
        /// </summary>
        /// <param name="page">页面</param>
        /// <param name="key">key</param>
        /// <param name="script">信息</param>
        public static void regesterAlertScript(Page page, string key, string message)
        {
            regesterScript(page, key, "alert('" + message + "');");
        }
    }
}