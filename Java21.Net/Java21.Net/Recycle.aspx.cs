using Java21.Logic;
using Java21.Model;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace Java21.Net
{
    public partial class Recycle : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                if (Session["LoginUser"] == null)
                    Response.Redirect("Login.aspx?url=" + Request.Url.AbsolutePath);
                if ((Session["LoginUser"] as UserInfo).Role == UserInfo.ROLE_ADMIN)
                {
                    getData();
                    linkClear.Visible = true;
                }
            }
        }

        private void getData()
        {
            JavaDLL dll = new JavaDLL();
            DataSet ds = dll.getRecycleKnowledge();
            gvRecycle.ShowFooter = (ds.Tables[0].Rows.Count <= gvRecycle.PageSize);
            gvRecycle.DataSource = ds;
            gvRecycle.DataBind();
            if (ds.Tables[0].Rows.Count > 0)
                litCount.Text = "(" + ds.Tables[0].Rows.Count + ")";
            else
                litCount.Text = "";
        }

        public string StringTruncat(string oldStr, int maxLength, string endWith)
        {
            if (string.IsNullOrEmpty(oldStr))
                return oldStr;
            if (maxLength < 1)
                return oldStr;
            if (oldStr.Length > maxLength)
            {
                string strTmp = oldStr.Substring(0, maxLength);
                if (string.IsNullOrEmpty(endWith))
                    return strTmp;
                else
                    return strTmp + endWith;
            }
            return oldStr;
        }

        protected void gvRecycle_PageIndexChanging(object sender, GridViewPageEventArgs e)
        {
            gvRecycle.PageIndex = e.NewPageIndex;
            getData();
        }

        protected void linkClear_Click(object sender, EventArgs e)
        {
            JavaDLL dll = new JavaDLL();
            dll.clearKnowledge();
            getData();
        }

        protected void lnkDelete_Click(object sender, EventArgs e)
        {
            int id = Convert.ToInt32(((LinkButton)sender).CommandName);
            JavaDLL dll = new JavaDLL();
            dll.delKnowledge(id);
            getData();
        }

        protected void lnkRestore_Click(object sender, EventArgs e)
        {
            int id = Convert.ToInt32(((LinkButton)sender).CommandName);
            JavaDLL dll = new JavaDLL();
            dll.restoreKnowledge(id);
            getData();
        }
    }
}