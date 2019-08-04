using Java21.Logic;
using Java21.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace Java21.Net
{
    public partial class Settings : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                if (Session["LoginUser"] == null)
                    Response.Redirect("Login.aspx?url=" + Request.Url.AbsolutePath);
                UserInfo info = (UserInfo) Session["LoginUser"];
                if (info != null)
                {
                    labUserName.Text = info.UserName;
                    labEmail.Text = info.Email;
                    txtName.Text = info.NickName;
                    txtRemark.Text = info.Mark;
                    ddlGender.SelectedValue = ""+info.Gender;
                    switch (info.Role)
                    {
                        case UserInfo.ROLE_ADMIN:
                            labRole.Text = "管理员";
                            break;
                        case UserInfo.ROLE_USER:
                            labRole.Text = "普通用户";
                            break;
                        case UserInfo.ROLE_CONF:
                            labRole.Text = "受限用户";
                            break;
                    }
                }
            }
        }

        public void RunScript(string script)
        {
            ScriptManager.RegisterStartupScript(this, GetType(), "ShowInfo", script, true);
        }

        protected void btnAmend_Click(object sender, EventArgs e)
        {
            if (txtName.Text.Trim().Length > 0)
            {
                UserInfo info = (UserInfo)Session["LoginUser"];
                info.NickName = txtName.Text.Trim();
                info.Mark = txtRemark.Text;
                info.Gender = Convert.ToInt32(ddlGender.SelectedValue);
                JavaDLL bll = new JavaDLL();
                if (bll.updateUser(info))
                    RunScript("alert('修改成功！');document.location.href='HomePage.aspx';");
                else
                    RunScript("alert('修改失败！');");
                Session["LoginUser"] = bll.Login(info.ID);
            }
            else
                RunScript("alert('请输入昵称');");
        }
    }
}