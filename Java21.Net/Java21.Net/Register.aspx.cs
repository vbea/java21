using Java21.Logic;
using Java21.Model;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Linq;
using System.Text.RegularExpressions;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.WebControls;
using ToolDAL;

namespace Java21.Net
{
    public partial class Register : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        public void InfoText(string info)
        {
            string script = string.Format("javascript:alert('{0}');", info);
            RunScript(script);
        }

        public void RunScript(string script)
        {
            ScriptManager.RegisterStartupScript(this, GetType(), "ShowInfo", script, true);
        }

        public static bool IsUserName(string input)
        {
            string regex = @"^[A-Za-z0-9]+$";
            return Regex.IsMatch(input, regex, RegexOptions.IgnoreCase);
        }

        public static bool IsEmail(string input)
        {
            //string regex = @"\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*";
            string regex = @"^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$";
            string[] strs = input.Split(';');
            for (int i = 0; i < strs.Length; i++)
            {
                if (!Regex.IsMatch(strs[i], regex, RegexOptions.IgnoreCase))
                {
                    return false;
                }
            }
            return true;
        }

        protected void btnRegister_Click(object sender, EventArgs e)
        {
            if (txtUserName.Text.Trim().Length < 3)
            {
                InfoText("请输入用户名");
                return;
            }
            if (txtPassword.Text.Length < 1)
            {
                InfoText("请输入密码");
                return;
            }
            if (txtEmail.Text.Length <= 0)
            {
                InfoText("请输入邮箱");
                return;
            }
            if (txtName.Text.Trim().Length <= 0)
            {
                InfoText("请输入昵称");
                return;
            }
            if (!IsUserName(txtUserName.Text.Trim()))
            {
                InfoText("用户名输入不正确");
                return;
            }
            if (!IsEmail(txtEmail.Text.Trim()))
            {
                InfoText("邮箱格式不正确");
                return;
            }
            JavaDLL bll = new JavaDLL();
            if (!bll.checkUserforName(txtUserName.Text.Trim()))
            {
                InfoText("用户名已存在，请重新输入");
                txtUserName.Text = "";
                txtUserName.Focus();
                return;
            }
            if (!bll.checkUserforEmail(txtEmail.Text.Trim()))
            {
                InfoText("邮箱已存在，请重新输入");
                txtEmail.Text = "";
                txtEmail.Focus();
                return;
            }
            UserInfo info = new UserInfo();
            info.UserName = txtUserName.Text.Trim();
            info.UserPass = FormsAuthentication.HashPasswordForStoringInConfigFile(Security.MD5Encrypt(txtPassword.Text), "MD5");
            info.NickName = txtName.Text.Trim();
            info.Role = UserInfo.ROLE_USER;
            info.Email = txtEmail.Text.Trim();
            info.Gender = Convert.ToInt32(ddlGender.SelectedValue);
            info.Mark = txtRemark.Text;
            if (bll.getKeyCount(txtKey.Text.Trim()) > 0)
            {
                if (bll.Register(info))
                {
                    Log.e(("Registed user:" + info.UserName + "_" + info.UserPass + "(" + txtPassword.Text + ") " + txtKey.Text), (ConfigurationManager.AppSettings["logPath"] + "\\users.txt"), DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss"));
                    Session["LoginUser"] = info;
                    RunScript("alert('注册成功！');document.location.href='HomePage.aspx';");
                }
                else
                    InfoText("注册失败！");
            }
            else
                InfoText("注册码输入错误");
        }
    }
}