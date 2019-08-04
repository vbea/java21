using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Data;
using ToolBLL;
using System.Globalization;

namespace Database_Tools
{
    public partial class ExecuteSQL : System.Web.UI.Page
    {
        private int connectID; //连接状态ID
        private List<string> strs_sys
        {
            get
            {
                return (List<string>)ViewState["strs_sys"];
            }
            set
            {
                ViewState["strs_sys"] = value;
            }
        }
        private List<string> strs_use //列表项
        {
            get
            {
                return (List<string>)ViewState["strs_use"];
            }
            set
            {
                ViewState["strs_use"] = value;
            }
        }
        private string[] SYSTEM_DB = { "master", "model", "msdb", "tempdb" };
        private string[] SQL_GO = { "go", "Go", "GO", "gO" };
        public List<DataTable> tables
        {
            get {
                return (List<DataTable>)Session["tmp_tables"];
            }

            set {
                Session["tmp_tables"] = value;
            }
        }
        public enum SQL_TYPE
        {
            SELECT = 1,
            UPDATE = 2,
            OBJECT = 3,
            NEWDB = 4
        }

        protected void Page_Load(object sender, EventArgs e)
        {
            if (Session["mConn"] == null)
                Response.Redirect("Default.aspx");
            if (!IsPostBack)
            {
                Session.Remove("eConn");
                getConnectID();
                showDatabase();
            }
        }

        //获取连接ID
        private void getConnectID()
        {
            if (Session["Server"] != null)
            {
                try
                {
                    //获取URL中的ID
                    connectID = Convert.ToInt32(Session["LinkID"]);
                }
                catch (Exception)
                {
                    connectID = -1;
                }
            }
            else
            {
                connectID = -1;
            }
        }

        //获取数据库列表
        private DataSet getDatabase()
        {
            if (connectID != -1 && Session["Server"] != null && Session["mConn"] != null)
            {
                if (connectID >= 0)
                {
                    ToolBll bll = new ToolBll(Session["mConn"].ToString());
                    if (bll.TestConnect()) //测试连接状态
                        return bll.getDatabaseList();
                    else
                        return null;
                }
                else
                    return null;
            }
            else
                return null;
        }

        //显示数据库列表
        private bool showDatabase()
        {
            try
            {
                //初始化列表项
                strs_sys = new List<string>();
                strs_use = new List<string>();
                strs_sys.Add("请选择...");
                strs_use.Add("请选择...");
                //遍历添加
                DataSet _ds = getDatabase();
                //清空列表中的项
                ddlDatabase.Items.Clear();
                if (_ds == null)
                {
                    ddlDatabase.DataSource = strs_sys;
                    ddlDatabase.DataBind();
                    return false;
                }
                foreach (DataRow row in _ds.Tables["database"].Rows)
                {
                    strs_sys.Add(row[0].ToString());
                    if (!findAnyString(SYSTEM_DB, row[0].ToString()))
                        strs_use.Add(row[0].ToString());
                }
                //绑定数据项
                if (chkSystemDatabase.Checked)
                    ddlDatabase.DataSource = strs_sys;
                else
                    ddlDatabase.DataSource = strs_use;
                ddlDatabase.DataBind();
                return true;
            }
            catch
            {
                return false;
            }
        }

        public bool findAnyString(string[] array, string value)
        {
            foreach (string s in array)
            {
                if (value.Equals(s))
                    return true;
            }
            return false;
        }

        public SQL_TYPE getSQLType(string sql)
        {
            if (sql.StartsWith("select", true, CultureInfo.CurrentCulture))
                return SQL_TYPE.SELECT;
            else if (sql.StartsWith("update", true, CultureInfo.CurrentCulture) || sql.StartsWith("delete", true, CultureInfo.CurrentCulture) || sql.StartsWith("insert", true, CultureInfo.CurrentCulture))
                return SQL_TYPE.UPDATE;
            else if (sql.StartsWith("create database", true, CultureInfo.CurrentCulture))
                return SQL_TYPE.NEWDB;
            else
                return SQL_TYPE.OBJECT;
        }

        protected void ddlDatabase_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (Session["mConn"] == null)
                Response.Redirect("Default.aspx");
            if (ddlDatabase.SelectedIndex > 0)
                Session["eConn"] = Session["mConn"].ToString().Replace("master", ddlDatabase.SelectedValue);
            else
                Session.Remove("eConn");
        }

        protected void btnRefresh_Click(object sender, EventArgs e)
        {
            showDatabase();
        }

        protected void chkSystemDatabase_CheckedChanged(object sender, EventArgs e)
        {
            string tmpSelectedValue = ddlDatabase.SelectedValue;
            if (strs_sys != null && strs_use != null)
            {
                if (chkSystemDatabase.Checked)
                    ddlDatabase.DataSource = strs_sys;
                else
                    ddlDatabase.DataSource = strs_use;
                ddlDatabase.DataBind();
            }
        }

        protected void btnBack_Click(object sender, EventArgs e)
        {
            Session.Remove("eConn");
            Session.Remove("tmp_tables");
            Response.Redirect("HomePage.aspx");
        }

        protected void btnExec_Click(object sender, EventArgs e)
        {
            if (txtMuSql.Text.Trim().Length > 0)
            {
                if (Session["eConn"] != null || txtMuSql.Text.Trim().StartsWith("use", true, CultureInfo.CurrentCulture) || txtMuSql.Text.Trim().StartsWith("create database", true, CultureInfo.CurrentCulture))
                {
                    string[] sqls = txtMuSql.Text.Trim().Split(SQL_GO, StringSplitOptions.RemoveEmptyEntries);
                    string results = "";
                    bool conti = true;
                    tables = new List<DataTable>();
                    foreach (string _sql in sqls)
                    {
                        if (!conti)
                        {
                            results += "<span class='err'>已终止执行</span><br/>";
                            break;
                        }
                        string sql = _sql.Trim();
                        if (sql.StartsWith("use", true, CultureInfo.CurrentCulture))
                        {
                            string[] data = sql.Split(' ');
                            if (data[0].ToLower().Equals("use") && data.Length > 1)
                            {
                                bool db = true;
                                foreach (ListItem item in ddlDatabase.Items)
                                {
                                    if (item.Text.ToLower().Equals(data[1].ToLower()))
                                    {
                                        ddlDatabase.SelectedValue = item.Value;
                                        Session["eConn"] = Session["mConn"].ToString().Replace("master", ddlDatabase.SelectedValue);
                                        results += "<span>切换成功</span><br/>";
                                        db = false;
                                        break;
                                    }
                                }
                                if (db)
                                {
                                    results += "<span class='err'>未找到数据库</span><br/>";
                                    conti = false;
                                }
                            }
                        }
                        else
                        {
                            try
                            {
                                ToolBll bll;
                                switch (getSQLType(sql))
                                {
                                    case SQL_TYPE.SELECT:
                                        bll = new ToolBll(Session["eConn"].ToString());
                                        tables.Add(bll.executeQueryToSelect(sql, tables.Count));
                                        results += "<span>查询成功</span><br/>";
                                        break;
                                    case SQL_TYPE.UPDATE:
                                        bll = new ToolBll(Session["eConn"].ToString());
                                        results += "<span class='blue'>" + bll.executeQueryToUdate(sql) + "行受影响</span><br/>";
                                        break;
                                    case SQL_TYPE.OBJECT:
                                        bll = new ToolBll(Session["eConn"].ToString());
                                        results += bll.executeQueryToObj(sql);
                                        break;
                                    case SQL_TYPE.NEWDB:
                                        bll = new ToolBll(Session["mConn"].ToString());
                                        bll.executeQueryToObj(sql);
                                        results = "<span>新增成功</span><br/>";
                                        showDatabase();
                                        break;
                                }
                            }
                            catch (Exception er)
                            {
                                results += "<span class='err'>" + er.Message + "</span><br/>";
                                conti = false;
                            }
                        }
                    }
                    txtResoult.InnerHtml = results;
                    if (tables.Count > 0)
                    {
                        divData.Visible = true;
                        ddlDataTable.DataSource = tables;
                        ddlDataTable.DataBind();
                        gdDataTable.DataSource = tables[0];
                        gdDataTable.DataBind();
                    }
                    else
                        divData.Visible = false;
                }
                else
                    Common.regesterAlertScript(this, "err", "请选择一个数据库");
            }
            else
                Common.regesterAlertScript(this, "err", "请输入SQL脚本");
        }

        protected void ddlDataTable_SelectedIndexChanged(object sender, EventArgs e)
        {
            gdDataTable.DataSource = tables[ddlDataTable.SelectedIndex];
            gdDataTable.DataBind();
        }
    }
}