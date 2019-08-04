using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using Java21.Logic;
using System.Web.Security;
using Java21.Model;

namespace Java21.Net
{
    public partial class Login : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            litError.Text = "";
        }

        protected void btnLogin_Click(object sender, EventArgs e)
        {
            if (txtUser.Text.Trim().Length <= 0)
            {
                litError.Text = "请输入账号(账号为用户名或邮箱)";
                txtUser.Focus();
                return;
            }
            if (txtPwd.Text.Trim().Length <= 0)
            {
                litError.Text = "请输入密码";
                txtPwd.Focus();
                return;
            }
            JavaDLL dll = new JavaDLL();
            UserInfo info = dll.Login(txtUser.Text.Trim());
            if (info == null)
            {
                litError.Text = "用户不存在";
                return;
            }
            if (info.Role == UserInfo.ROLE_CONF)
            {
                litError.Text = "账户已被限制使用";
                return;
            }
            if (!info.Valid)
            {
                litError.Text = "无效的用户";
                return;
            }
            string s = FormsAuthentication.HashPasswordForStoringInConfigFile(Security.MD5Encrypt(txtPwd.Text), "MD5");
            if (s.Equals(info.UserPass))
            {
                Session["LoginUser"] = info;
                if (Request.QueryString["url"] != null && Request.QueryString["url"].Length > 0)
                    Response.Redirect(Request.QueryString["url"]);
                else
                    Response.Redirect("HomePage.aspx");
            }
            else
            {
                litError.Text = "密码输入不正确";
                txtPwd.Text = string.Empty;
                txtPwd.Focus();
            }
        }
    }
}