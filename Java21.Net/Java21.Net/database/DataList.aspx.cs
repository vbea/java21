using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Data;
using ToolBLL;

namespace Database_Tools
{
    public partial class DataList : System.Web.UI.Page
    {
        public string tName;
        public string mName;
        protected void Page_Load(object sender, EventArgs e)
        {
            if (Request.QueryString["table"] != null)
            {
                mName = Request.QueryString["table"];
                tName = "表名：";
            }
            else if (Request.QueryString["view"] != null)
            {
                mName = Request.QueryString["view"];
                tName = "视图：";
            }
            else
            {
                litName.Text = "不支持的链接！";
                panData.Visible = false;
            }

            if (Request.QueryString["schemas"] != null)
            {
                if (Request.QueryString["schemas"].Length > 0)
                    InitBind("[" + Request.QueryString["schemas"] + "].[" + mName + "]");
                else
                    InitBind("[" + Request.QueryString["table"] + "]");
            }
            else
                InitBind("[" + Request.QueryString["table"] + "]");
            
        }

        private void InitBind(string table)
        {
            if (Session["sConn"] == null)
                return;
            ToolBll _bll = new ToolBll(Session["sConn"].ToString());
            DataSet ds = _bll.getDataTopList(table);
            if (ds != null)
            {
                gvDataList.DataSource = ds.Tables[0];
                gvDataList.DataBind();
                litName.Text = tName + mName;
                if (gvDataList.PageCount > 1)
                    litPage.Text = "页码：" + (gvDataList.PageIndex + 1) + "/" + gvDataList.PageCount;
                hidTableName.Value = table;
            }
        }

        protected void gvDataList_PageIndexChanging(object sender, GridViewPageEventArgs e)
        {
            gvDataList.PageIndex = e.NewPageIndex;
            InitBind(hidTableName.Value);
        }
    }
}