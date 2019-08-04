using Java21.Logic;
using Java21.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Security;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace Java21.Net
{
    public partial class EditUser : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                if (Session["LoginUser"] == null)
                    Response.End();
                if ((Session["LoginUser"] as UserInfo).Role == UserInfo.ROLE_ADMIN)
                {
                    if (Request.QueryString["id"] != null)
                        getUser(Convert.ToInt32(Request.QueryString["id"]));
                    else
                        btnAmend.Enabled = false;
                }
                else
                    btnAmend.Visible = false;
            }
        }

        public void getUser(int id)
        {
            JavaDLL bll = new JavaDLL();
            UserInfo info = Session["LoginUser"] as UserInfo;
            UserInfo Users = bll.Login(id);
            Session["TmpUser"] = Users;
            if (Users != null)
            {
                txtUserName.Text = Users.UserName;
                txtNickName.Text = Users.NickName;
                labEmail.Text = Users.Email;
                txtRemark.Text = Users.Mark;
                ddlGender.SelectedValue = "" + Users.Gender;
                ddlRole.SelectedValue = "" + Users.Role;
                chkValid.Checked = Users.Valid;
                if (Users.UserName.Equals("admin") || info.UserName.Equals(Users.UserName))
                {
                    txtUserName.Enabled =
                    ddlRole.Enabled =
                    linPassword.Visible =
                    chkValid.Enabled = false;
                }
            }
        }

        protected void btnAmend_Click(object sender, EventArgs e)
        {
            UserInfo info = (UserInfo)Session["TmpUser"];
            info.UserName = txtUserName.Text;
            info.NickName = txtNickName.Text;
            info.Mark = txtRemark.Text;
            info.Gender = Convert.ToInt32(ddlGender.SelectedValue);
            info.Role = Convert.ToInt32(ddlRole.SelectedValue);
            info.Valid = chkValid.Checked;
            JavaDLL bll = new JavaDLL();
            if (bll.updateFillUser(info))
                ClientScript.RegisterStartupScript(GetType(), "tip", "alert('修改成功！');top.closeEditDialog();", true);
            else
                ClientScript.RegisterStartupScript(GetType(), "tip", "alert('修改失败！');", true);
        }

        protected void linPassword_Click(object sender, EventArgs e)
        {
            if (txtPassword.Text.Trim().Length > 0)
            {
                UserInfo info = (UserInfo)Session["TmpUser"];
                info.UserPass = FormsAuthentication.HashPasswordForStoringInConfigFile(Security.MD5Encrypt(txtPassword.Text), "MD5");
                labPassword.Visible = true;
                trPass.Visible = false;
            }
        }
    }
}