using Java21.Logic;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace Java21.Net
{
    public partial class Video : System.Web.UI.Page
    {
        string url = "<p>{0}</p><embed src='{1}' quality='high' width='480' height='400' align='middle' allowFullScreen='true' quality='high' allowScriptAccess='always' type='application/x-shockwave-flash' />";
        protected void Page_Load(object sender, EventArgs e)
        {
            if (Session["LoginUser"] == null)
                Response.Redirect("Login.aspx?url=" + Request.Url.AbsolutePath);
            if (!IsPostBack)
                getData();
        }

        private void getData()
        {
            JavaDLL dll = new JavaDLL();
            listVideo.DataSource = dll.getAllVideo();
            listVideo.DataTextField = "name";
            listVideo.DataValueField = "url";
            listVideo.DataBind();
        }

        protected void listVideo_SelectedIndexChanged(object sender, EventArgs e)
        {
            panFlash.InnerHtml = string.Format(url, listVideo.SelectedItem.Text, listVideo.SelectedValue);
        }
    }
}