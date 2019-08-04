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
    public partial class Feedback : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                if (Session["LoginUser"] == null)
                    return;
                if ((Session["LoginUser"] as UserInfo).Role == UserInfo.ROLE_ADMIN)
                    getData();
            }
        }

        public void getData()
        {
            JavaDLL dll = new JavaDLL();
            gvFeed.DataSource = dll.getFeedback();
            gvFeed.DataBind();
        }

        protected void gvFeed_PageIndexChanging(object sender, GridViewPageEventArgs e)
        {
            gvFeed.PageIndex = e.NewPageIndex;
            getData();
        }

        protected void linkDelete_Click(object sender, EventArgs e)
        {
            int id = Convert.ToInt32(((LinkButton)sender).CommandName);
            JavaDLL dll = new JavaDLL();
            if (dll.delFeedback(id))
                getData();
            else
                ClientScript.RegisterStartupScript(GetType(), "err", "alert('删除失败！');", true);
        }
    }
}