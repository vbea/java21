using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using Java21.Model;

namespace Java21.Net
{
    public partial class Java : System.Web.UI.MasterPage
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                UserInfo info = (UserInfo)Session["LoginUser"];
                if (info == null)
                {
                    divLogin.Visible =
                    listUser.Visible =
                    listFeed.Visible =
                    listQuot.Visible = false;
                    panSingup.Visible = true;
                    return;
                }
                else if (info.Role == UserInfo.ROLE_CONF)
                    Response.Redirect("Login.aspx?url=" + Request.Url.AbsolutePath);
                if (info.Role != UserInfo.ROLE_ADMIN)
                {
                    listUser.Visible = 
                    listQuot.Visible =
                    listFeed.Visible = false;
                }
                labUser.Text = info.NickName;
            }
        }

        protected void logout_Click(object sender, EventArgs e)
        {
            Session.RemoveAll();
            Response.Redirect("Login.aspx");
        }
    }
}