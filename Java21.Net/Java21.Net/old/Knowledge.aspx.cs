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
    public partial class Knowledge : System.Web.UI.Page
    {
        public bool Visi
        {
            get
            {
                return Convert.ToBoolean(ViewState["visible"]);
            }
            set
            {
                ViewState["visible"] = value;
            }
        }
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                if (Session["LoginUser"] != null)
                {
                    Visi = (Session["LoginUser"] as UserInfo).Role == UserInfo.ROLE_ADMIN;
                    getData();
                }
            }
        }

        private void getData()
        {
            JavaDLL dll = new JavaDLL();
            gvKey.DataSource = dll.getArticle();
            gvKey.DataBind();
        }

        private void getData(JavaDLL dll)
        {
            gvKey.DataSource = dll.getArticle();
            gvKey.DataBind();
        }

        protected string getFoot(object cdate, object cuser, object edate, object euser)
        {
            if (euser.Equals(DBNull.Value))
                return "<span class='gray'>" + cuser + "</span> 于 " + Convert.ToDateTime(cdate).ToString("yyyy-MM-dd HH:mm:ss") + " 发表";
            else
                return "<span class='gray'>" + euser + "</span> 于 " + Convert.ToDateTime(edate).ToString("yyyy-MM-dd HH:mm:ss") + " 修改";
        }
        protected void gvKey_PageIndexChanging(object sender, GridViewPageEventArgs e)
        {
            gvKey.PageIndex = e.NewPageIndex;
            getData();
        }

        protected void hidAjax_Click(object sender, EventArgs e)
        {
            getData();
        }

        protected void linkDelete_Click(object sender, EventArgs e)
        {
            int id = Convert.ToInt32(((LinkButton)sender).CommandName);
            JavaDLL dll = new JavaDLL();
            dll.deleteArticle(id);
            getData(dll);
        }

        protected void linRecycle_Click(object sender, EventArgs e)
        {
            Response.Redirect("Recycle.aspx");
        }

        protected void gvKey_RowDataBound(object sender, GridViewRowEventArgs e)
        {
            if (e.Row.RowIndex >= 0)
            {
                e.Row.Attributes.Add("onmouseover", "currentcolor=this.style.backgroundColor;this.style.backgroundColor='#E7F7FF';");
                e.Row.Attributes.Add("onmouseout", "this.style.backgroundColor=currentcolor;");
            }
        }
    }
}