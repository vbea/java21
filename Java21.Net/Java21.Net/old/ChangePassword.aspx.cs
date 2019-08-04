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
    public partial class ChangePassword : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                if (Request.QueryString["action"].Equals("amend") && Session["LoginUser"] != null)
                {}
                else
                {
                    Response.Redirect("Login.aspx");
                }
            }
        }

        public void RunScript(string script)
        {
            ScriptManager.RegisterStartupScript(this, GetType(), "ShowInfo", script, true);
        }

        protected void btnChangePass_Click(object sender, EventArgs e)
        {
            if (txtOldPass.Text.Length > 0)
            {
                if (txtNewPass.Text.Length > 0 && txtPassword.Text.Length > 0)
                {
                    if (txtNewPass.Text.Equals(txtPassword.Text))
                    {
                        string oldPass = FormsAuthentication.HashPasswordForStoringInConfigFile(Security.MD5Encrypt(txtOldPass.Text), "MD5");
                        UserInfo info = (UserInfo) Session["LoginUser"];
                        if (oldPass.Equals(info.UserPass))
                        {
                            string newPass = FormsAuthentication.HashPasswordForStoringInConfigFile(Security.MD5Encrypt(txtPassword.Text), "MD5");
                            JavaDLL bll = new JavaDLL();
                            if (bll.changePassword(info.ID, newPass))
                            {
                                Session.RemoveAll();
                                RunScript("alert('修改成功！请用新密码登录');top.closePasswordDialog();");
                            }
                            else
                                RunScript("alert('修改失败！');");
                        }
                        else
                            RunScript("alert('原密码输入不正确');");
                    }
                    else
                        RunScript("alert('两次输入的密码不一致');");
                }
                else
                    RunScript("alert('请输入新密码');");
            }
            else
                RunScript("alert('请输入原密码');");
        }
    }
}