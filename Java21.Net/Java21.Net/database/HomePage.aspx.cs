using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Data;
using System.Data.SqlClient;
using ToolBLL;
using ToolModel;
using System.Collections;
using System.Threading;
using System.Globalization;

namespace Database_Tools
{
    public partial class HomePage : System.Web.UI.Page
    {
        #region Initialization
        private int cid = -1;
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
        private string[] stables;
        private const string TYPE_TABLE = "table";
        private const string TYPE_VIEW = "view";
        private const string TYPE_PROC = "proc";
        private string[] SYSTEM_DB = { "master", "model", "msdb", "tempdb" };
        private ToolBll _bll;
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                getConnectID();
                showDatabase();
            }
        }
        #endregion

        #region 公共方法
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
                    if (!findAnyString(SYSTEM_DB,row[0].ToString()))
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

        //初始化界面
        private void closeSelect()
        {
            panSelect0.Visible =
            panSelect1.Visible =
            panInsert.Visible =
            panUpdate.Visible =
            gdSelectView.Visible =
            gdUpdateView.Visible =
            panUpdateButton.Visible =
            panNewTable.Visible =
            panProcParam.Visible =
            panProcExec.Visible =
            panProcExecData.Visible =
            panProcDefine.Visible =
            panSettings.Visible =
            rdbCustom.Checked =
            rdbNewView.Checked = 
            rdbNewProc.Checked = false;
            txtWhere.Text =
            labHeaderName.Text =
            txtUpdateWhere.Text =
            labProcExec.Text = 
            labUpdate.Text =
            litInsertTip.Text =
            litUpdateTips.Text =
            labReConnect.Text =
            labTips.Text = string.Empty;
            rdbNewTable.Checked =
            btnInsertAction.Enabled = true;
        }

        //显示输入框状态
        protected bool showState(object id)
        {
            switch (Convert.ToInt32(id))
            {
                case 0:
                    return true;
                case 1:
                    return false;
                default:
                    return true;
            }
        }

        //设置排除按钮的ID
        protected int getContinuousID()
        {
            cid++;
            return cid;
        }

        //获取完整表名
        public string getAbsoluteTableName(string schemas, string table)
        {
            return "[" + schemas + "].[" + table + "]";
        }

        /// <summary>
        /// 查找有无数据库
        /// </summary>
        /// <param name="items">下拉框选项</param>
        /// <param name="database">数据库名</param>
        /// <returns>Index索引，没有则返回-1</returns>
        private int ExsitDatabase(ListItemCollection items,string database)
        {
            for (int i = 0; i < items.Count; i++)
            {
                if (items[i].Text.ToLower().Equals(database.ToLower()))
                    return i;
            }
            return -1;
        }

        public void getSconn()
        {
            if (Session["mConn"] == null)
                Response.Redirect("Default.aspx");
            else if (Session["sConn"] == null)
                Session["sConn"] = Session["mConn"].ToString().Replace("master", ddlDatabase.SelectedValue);
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
        #endregion

        #region 外层界面操作
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
            if (ddlDatabase.Items.IndexOf(new ListItem(tmpSelectedValue, tmpSelectedValue)) > 0)
                ddlDatabase.SelectedValue = tmpSelectedValue;
            else
                lsbDataTable.Items.Clear();
        }

        protected void ddlDatabase_SelectedIndexChanged(object sender, EventArgs e)
        {
            if (Session["mConn"] == null)
                Response.Redirect("Default.aspx");
            if (ddlDatabase.SelectedIndex > 0)
            {
                Session["sConn"] = Session["mConn"].ToString().Replace("master", ddlDatabase.SelectedValue);
                if (hidTabType.Value.Equals(""))
                    hidTabType.Value = TYPE_TABLE;
                BindDataList(hidTabType.Value);
                Session["Database"] = ddlDatabase.SelectedValue;
            }
            else
            {
                panTabControl.Visible = false;
                panToolBar.Visible = false;
                Session.Remove("Database");
            }
            panMessage.Visible = false;
            closeSelect();
            lnkDelete.Visible = false;
        }

        protected void Timer1_Tick(object sender, EventArgs e)
        {
            if (panInsert.Visible)
            {
                if (litInsertTip.Text == "新增成功！" && chkUpdateEmpty.Checked)
                {
                    //清空输入框
                    for (int i = 0; i < gdInsertView.Rows.Count; i++)
                    {
                        ((TextBox)gdInsertView.Rows[i].FindControl("txtDataValue")).Text = "";
                    }
                }
                litInsertTip.Text = "";
            }
            else if (panSelect0.Visible)
                labTips.Text = "";
            else if (panUpdate.Visible)
                labUpdate.Text = "";
            else if (panNewTable.Visible)
                labNewTableTip.Text = "";
            Timer1.Enabled = false;
        }

        protected void btnRefresh_Click(object sender, EventArgs e)
        {
            closeSelect();
            showDatabase();
            panMessage.Visible =
            panTabControl.Visible =
            panToolBar.Visible = false;
        }
        #endregion

        #region 左边列表界面操作
        private void BindDataList(string type)
        {
            DataSet ds;
            getSconn();
            _bll = new ToolBll(Session["sConn"].ToString());
            ds = _bll.getDataTableList();
            if (ds == null)
            {
                panToolBar.Visible =
                panTabControl.Visible = false;
                return;
            }
            lsbDataTable.Items.Clear();
            lsbDataView.Items.Clear();
            lsbProcedures.Items.Clear();
            btnSelectTable.CssClass = "btn_tab";
            foreach (DataRow row in ds.Tables["datatable"].Rows)
                lsbDataTable.Items.Add(new ListItem(row["name"].ToString(), row["schemas"].ToString() + "." + row["name"].ToString()));
            ds = _bll.getDataViewList();
            if (ds.Tables["datatable"].Rows.Count > 0)
            {
                btnSelectView.Visible = true;
                btnSelectView.CssClass = "btn_tab";
                foreach (DataRow row in ds.Tables["datatable"].Rows)
                    lsbDataView.Items.Add(new ListItem(row["name"].ToString(), row["schemas"].ToString() + "." + row["name"].ToString()));
            }
            else
            {
                btnSelectView.Visible = false;
                if (type == TYPE_VIEW)
                    type = TYPE_TABLE;
            }
            ds = _bll.getDataProcedureList();
            if (ds.Tables["datatable"].Rows.Count > 0)
            {
                btnSelectProcedures.Visible = true;
                btnSelectProcedures.CssClass = "btn_tab";
                foreach (DataRow row in ds.Tables["datatable"].Rows)
                    lsbProcedures.Items.Add(new ListItem(row["name"].ToString(), row["schemas"].ToString() + "." + row["name"].ToString()));
            }
            else
            {
                btnSelectProcedures.Visible = false;
                if (type == TYPE_PROC)
                    type = TYPE_TABLE;
            }
            switch (type)
            {
                case TYPE_TABLE:
                {
                    mlvList.SetActiveView(viewTable);
                    hidTabType.Value = TYPE_TABLE;
                    btnSelectTable.CssClass = "btn_tab_pause";
                    break;
                }
                case TYPE_VIEW:
                {
                    mlvList.SetActiveView(viewView);
                    hidTabType.Value = TYPE_VIEW;
                    btnSelectView.CssClass = "btn_tab_pause";
                    break;
                }
                case TYPE_PROC:
                {
                    mlvList.SetActiveView(viewProcedure);
                    hidTabType.Value = TYPE_PROC;
                    btnSelectProcedures.CssClass = "btn_tab_pause";
                    break;
                }
                default:
                {
                    mlvList.SetActiveView(viewTable);
                    hidTabType.Value = TYPE_TABLE;
                    btnSelectTable.CssClass = "btn_tab_pause";
                    break;
                }
            }
            panToolBar.Visible =
            panTabControl.Visible = true;
        }
        protected void lsbDataTable_SelectedIndexChanged(object sender, EventArgs e)
        {
            panMessage.Visible = true;
            lsbDataView.SelectedIndex = -1;
            lsbProcedures.SelectedIndex = -1;
            labTableName.Text = "表名：" + lsbDataTable.SelectedItem.Text;
            hidTabType.Value = TYPE_TABLE;
            lnkDelete.OnClientClick = "return confirm('确定要删除该表？（删除后将无法恢复）');";
            stables = lsbDataTable.SelectedValue.Split('.');
            hidSchemasName.Value = stables[0];
            hidTablesName.Value = stables[1];
            getSconn();
             _bll = new ToolBll(Session["sConn"].ToString());
            litDetail.Text = _bll.getTableCount(getAbsoluteTableName(hidSchemasName.Value, hidTablesName.Value));
            btnShowTop100.NavigateUrl = "DataList.aspx?table=" + hidTablesName.Value + "&schemas=" + hidSchemasName.Value;
            closeSelect();
            panTableCount.Visible = lnkDelete.Visible = btnShowTop100.Visible = btnNewSelect.Visible = btnNewData.Visible = btnUpdate.Visible = btnDelete.Visible = true;
            btnProcparam.Visible = btnProcAction.Visible = btnDefine.Visible = false;
        }

        protected void lsbDataView_SelectedIndexChanged(object sender, EventArgs e)
        {
            panMessage.Visible = true;
            lsbDataTable.SelectedIndex = -1;
            lsbProcedures.SelectedIndex = -1;
            labTableName.Text = "视图：" + lsbDataView.SelectedItem.Text;
            hidTabType.Value = TYPE_VIEW;
            lnkDelete.OnClientClick = "return confirm('确定要删除该视图？（删除后将无法恢复）');";
            stables = lsbDataView.SelectedValue.Split('.');
            hidSchemasName.Value = stables[0];
            hidTablesName.Value = stables[1];
            getSconn();
             _bll = new ToolBll(Session["sConn"].ToString());
            litDetail.Text = _bll.getTableCount(getAbsoluteTableName(hidSchemasName.Value, hidTablesName.Value));
            stables = lsbDataView.SelectedValue.Split('.');
            btnShowTop100.NavigateUrl = "DataList.aspx?view=" + hidTablesName.Value + "&schemas=" + hidSchemasName.Value;
            closeSelect();
            panTableCount.Visible = btnShowTop100.Visible = btnNewSelect.Visible = lnkDelete.Visible = true;
            btnNewData.Visible = btnUpdate.Visible = btnDelete.Visible = btnProcparam.Visible = btnProcAction.Visible = btnDefine.Visible = false;
        }

        protected void lsbProcedures_SelectedIndexChanged(object sender, EventArgs e)
        {
            panMessage.Visible = true;
            lsbDataTable.SelectedIndex = -1;
            lsbDataView.SelectedIndex = -1;
            labTableName.Text = "存储过程：" + lsbProcedures.SelectedItem.Text;
            hidTabType.Value = TYPE_PROC;
            lnkDelete.OnClientClick = "return confirm('确定要删除该存储过程？（删除后将无法恢复）');";
            stables = lsbProcedures.SelectedValue.Split('.');
            hidSchemasName.Value = stables[0];
            hidTablesName.Value = stables[1];
            //btnShowTop100.NavigateUrl = "DataList.aspx?proc=" + hidTablesName.Value + "&schemas=" + hidSchemasName.Value;
            closeSelect(); hidProcExec.Value = "";
            btnProcparam.Visible = btnProcAction.Visible = btnDefine.Visible = lnkDelete.Visible = true;
            panTableCount.Visible = btnShowTop100.Visible = btnNewSelect.Visible = btnNewData.Visible = btnUpdate.Visible = btnDelete.Visible = false;
        }

        protected void lnkDelete_Click(object sender, EventArgs e)
        {
            getSconn();
             _bll = new ToolBll(Session["sConn"].ToString());
            switch (hidTabType.Value)
            {
                case TYPE_TABLE:
                {
                    if (lsbDataTable.SelectedIndex >= 0 && lsbDataTable.SelectedValue != null)
                        _bll.deleteTable(getAbsoluteTableName(hidSchemasName.Value, hidTablesName.Value));
                    break;
                }
                case TYPE_VIEW:
                {
                    if (lsbDataView.SelectedIndex >= 0 && lsbDataView.SelectedValue != null)
                        _bll.deleteView(getAbsoluteTableName(hidSchemasName.Value, hidTablesName.Value));
                    break;
                }
                case TYPE_PROC:
                {
                    if (lsbProcedures.SelectedIndex >= 0 && lsbProcedures.SelectedValue != null)
                        _bll.deleteProcedure(getAbsoluteTableName(hidSchemasName.Value, hidTablesName.Value));
                    break;
                }
            }
            //Changed on 2015.06.04
            BindDataList(hidTabType.Value);
            panToolBar.Visible = true;
            panMessage.Visible = false;
            lnkDelete.Visible = false;

            closeSelect();
        }

        protected void lnkNewTable_Click(object sender, EventArgs e)
        {
            if (!panNewTable.Visible)
            {
                closeSelect();
                labHeaderName.Text = "新建表";
                panNewTable.Visible = true;
                panMessage.Visible = false;
                labNameTable_Name.Visible = txtNewTable_Name.Visible = true;
                labNameTable_Name.Text = "表名：";
                labColumnList.Text = "列定义：<br />（每一列之间用逗号分隔）";
            }
        }

        protected void btnSelectTable_Click(object sender, EventArgs e)
        {
            if (mlvList.GetActiveView() != viewTable)
            {
                mlvList.SetActiveView(viewTable);
                btnSelectTable.CssClass = "btn_tab_pause";
                btnSelectView.CssClass = btnSelectProcedures.CssClass = "btn_tab";
                lnkDelete.Visible = false;
                hidTabType.Value = TYPE_TABLE;
            }
        }

        protected void btnSelectView_Click(object sender, EventArgs e)
        {
            if (mlvList.GetActiveView() != viewView)
            {
                mlvList.SetActiveView(viewView);
                btnSelectView.CssClass = "btn_tab_pause";
                btnSelectTable.CssClass = btnSelectProcedures.CssClass = "btn_tab";
                lnkDelete.Visible = false;
                hidTabType.Value = TYPE_VIEW;
            }
        }

        protected void btnSelectProcedures_Click(object sender, EventArgs e)
        {
            if (mlvList.GetActiveView() != viewProcedure)
            {
                mlvList.SetActiveView(viewProcedure);
                btnSelectProcedures.CssClass = "btn_tab_pause";
                btnSelectTable.CssClass = btnSelectView.CssClass = "btn_tab";
                lnkDelete.Visible = false;
                hidTabType.Value = TYPE_PROC;
            }
        }

        #endregion

        #region 查询界面操作
        protected void btnClose_Click(object sender, EventArgs e)
        {
            closeSelect();
        }

        protected void btnNewSelect_Click(object sender, EventArgs e)
        {
            if (!panSelect0.Visible)
            {
                ddlfield.Items.Clear();
                closeSelect();
                labHeaderName.Text = "查询数据";
                //strs_sys = new ListItemCollection();
                //strs_sys.Add("请选择");
                getSconn();
                 _bll = new ToolBll(Session["sConn"].ToString());
                panSelect0.Visible = true;
                foreach (DataRow row in _bll.getDatatablefield(getAbsoluteTableName(hidSchemasName.Value, hidTablesName.Value)).Tables["field"].Rows)
                {
                    ddlfield.Items.Add(new ListItem(row[0].ToString(), "[" + row[0].ToString() + "]"));
                }
            }
        }

        protected void btnAction_Click(object sender, EventArgs e)
        {
            /*if (ddlfield.SelectedIndex == 0)
            {
                labTips.Text = "请选择条件字段";
                Timer1.Enabled = true;
                return;
            }*/
            getSconn();
             _bll = new ToolBll(Session["sConn"].ToString());
            DataSet _ds = _bll.selectData(getAbsoluteTableName(hidSchemasName.Value, hidTablesName.Value), ddlfield.SelectedValue, txtWhere.Text, chkLike.Checked);
            if (_ds != null)
            {
                panSelect1.Visible = true;
                gdSelectView.DataSource = _ds;
                gdSelectView.DataBind();
            }
            else
            {
                panSelect1.Visible = false;
                labTips.Text = "条件输入不正确！";
                Timer1.Enabled = true;
            }
            gdSelectView.Visible = true;
        }
        #endregion

        #region 新增界面操作
        protected void btnNewData_Click(object sender, EventArgs e)
        {
            if (!panInsert.Visible)
            {
                closeSelect();
                labHeaderName.Text = "新增数据";
                panInsert.Visible = true;
                getSconn();
                 _bll = new ToolBll(Session["sConn"].ToString());
                gdInsertView.DataSource = _bll.getInsertFiled(hidTablesName.Value, hidSchemasName.Value);
                gdInsertView.DataBind();
            }
        }

        protected void btnInsertAction_Click(object sender, EventArgs e)
        {
            string[] filds = new string[gdInsertView.Rows.Count];
            string[] strs = new string[gdInsertView.Rows.Count];
            for (int i = 0; i < gdInsertView.Rows.Count; i++)
            {
                //Changed 2015.06.04
                //strs[i] = ((TextBox)gdInsertView.Rows[i].FindControl("txtDataValue")).Text;
                if (((TextBox)gdInsertView.Rows[i].FindControl("txtDataValue")).Enabled)
                {
                    filds[i] = ((Label)gdInsertView.Rows[i].FindControl("labInsertColumn")).Text;
                    strs[i] = ((TextBox)gdInsertView.Rows[i].FindControl("txtDataValue")).Text;
                }
                else filds[i] = "";
            }
            getSconn();
             _bll = new ToolBll(Session["sConn"].ToString());
            if (_bll.InsertValue(getAbsoluteTableName(hidSchemasName.Value, hidTablesName.Value), filds, strs))
            {
                litInsertTip.Text = "新增成功！";
                litDetail.Text = _bll.getTableCount(getAbsoluteTableName(hidSchemasName.Value, hidTablesName.Value));
            }
            else
                litInsertTip.Text = "新增失败，请检查输入的数据类型是否匹配！";
            Timer1.Enabled = true;
        }

        protected void linkInsertRejectColumn_Click(object sender, EventArgs e)
        {
            LinkButton link = (LinkButton)sender;
            if (link.Text.Equals("排除"))
            {
                ((TextBox)gdInsertView.Rows[int.Parse(link.CommandName)].FindControl("txtDataValue")).Enabled = false;
                link.Text = "包含";
            }
            else
            {
                ((TextBox)gdInsertView.Rows[int.Parse(link.CommandName)].FindControl("txtDataValue")).Enabled = true;
                link.Text = "排除";
            }
            bool dtate = false;
            for (int i = 0; i < gdInsertView.Rows.Count; i++)
            {
                if (!((LinkButton)gdInsertView.Rows[i].FindControl("linkInsertRejectColumn")).Text.Equals("包含"))
                {
                    dtate = true;
                    break;
                }
            }
            btnInsertAction.Enabled = dtate;
        }

        protected void gdInsertView_DataBound(object sender, EventArgs e)
        {
            for (int i = 0; i < gdInsertView.Rows.Count; i++)
            {
                if (!((Label)gdInsertView.Rows[i].FindControl("labInsertColumnIsNull")).Text.Trim().ToLower().Equals("yes"))
                {
                    ((LinkButton)gdInsertView.Rows[i].FindControl("linkInsertRejectColumn")).Enabled = false;
                    ((LinkButton)gdInsertView.Rows[i].FindControl("linkInsertRejectColumn")).Text = "--";
                }
            }
        }
        #endregion

        #region 修改&删除界面操作
        protected void btnUpdate_Click(object sender, EventArgs e)
        {
            if (!panUpdate.Visible)
            {
                closeSelect();
                ddlUpdateFiled.Items.Clear();
                labHeaderName.Text = "修改&删除数据";
                panUpdate.Visible = true;
                getSconn();
                 _bll = new ToolBll(Session["sConn"].ToString());
                //绑定列名列表
                //strs_sys = new ListItemCollection();
                foreach (DataRow row in _bll.getDatatablefield(getAbsoluteTableName(hidSchemasName.Value, hidTablesName.Value)).Tables["field"].Rows)
                {
                    //Changed on 2015.06.04
                    ddlUpdateFiled.Items.Add(new ListItem(row[0].ToString(), "[" + row[0].ToString() + "]"));
                }
            }
        }

        protected void btnUSelect_Click(object sender, EventArgs e)
        {
            int count = 0;
            txtUpdateHWhere.Text = txtUpdateWhere.Text;
            getSconn();
             _bll = new ToolBll(Session["sConn"].ToString());
            if (_bll.getTmpDateCount(getAbsoluteTableName(hidSchemasName.Value, hidTablesName.Value), ddlUpdateFiled.SelectedValue, txtUpdateHWhere.Text, out count))
            {
                gdUpdateView.Visible = true;
                gdUpdateView.DataSource = _bll.getUpdateFiled(hidTablesName.Value, hidSchemasName.Value, ddlUpdateFiled.SelectedValue, txtUpdateHWhere.Text);
                gdUpdateView.DataBind();
                panUpdateButton.Visible = true;
                gdUpdateView.Focus();
                if (count > 1)
                {
                    litUpdateTips.Text = "您当前的更改可能会影响到" + count + "条数据";
                    litUpdateTips.Visible = true;
                    btnDUpdate.OnClientClick = "return confirm('此更新可能会影响" + count + "条数据，请做好列的排除工作，确定更新这些数据吗？')";
                }
                else
                {
                    litUpdateTips.Text = "";
                    btnDUpdate.OnClientClick = "return confirm('确定要更新该数据吗？')";
                }
            }
            else
            {
                labUpdate.Text = "没有记录！";
                panUpdateButton.Visible = false;
                Timer1.Enabled = true;
            }
        }

        protected void btnDDelete_Click(object sender, EventArgs e)
        {
            int count = 0;
            getSconn();
             _bll = new ToolBll(Session["sConn"].ToString());
            if (_bll.deleteViewData(getAbsoluteTableName(hidSchemasName.Value, hidTablesName.Value), ddlUpdateFiled.SelectedValue, txtUpdateHWhere.Text, out count))
            {
                labUpdate.Text = count + "条数据成功删除！";
                litDetail.Text = _bll.getTableCount(getAbsoluteTableName(hidSchemasName.Value, hidTablesName.Value));
                Timer1.Enabled = true;
                panUpdateButton.Visible = gdUpdateView.Visible = false;
            }
            else
            {
                labUpdate.Text = "删除失败！";
                Timer1.Enabled = true;
            }
        }

        protected void btnDUpdate_Click(object sender, EventArgs e)
        {
            string[] strs = new string[gdUpdateView.Rows.Count];
            List<SqlParameter> paras = new List<SqlParameter>();
            for (int i = 0; i < gdUpdateView.Rows.Count; i++)
            {
                if (((TextBox)gdUpdateView.Rows[i].FindControl("txtDataUpdateValue")).Enabled)
                {
                    //Changed on 2015.06.04
                    strs[i] = ("[" + ((Label)gdUpdateView.Rows[i].FindControl("labUpdateColumn")).Text + "]=@value" + i);
                    paras.Add(new SqlParameter("@value" + i, ((TextBox)gdUpdateView.Rows[i].FindControl("txtDataUpdateValue")).Text));
                }
                else
                    strs[i] = "";
            }
            getSconn();
             _bll = new ToolBll(Session["sConn"].ToString());
            int rows;
            if (_bll.updateViewData(getAbsoluteTableName(hidSchemasName.Value, hidTablesName.Value), strs, ddlUpdateFiled.SelectedValue, txtUpdateHWhere.Text, paras, out rows))
            {
                labUpdate.Text = "修改成功，" + rows + "条数据被修改！";
                //panUpdateButton.Visible = false;
            }
            else
                labUpdate.Text = "修改失败，请检查输入的数据类型是否匹配！";
            Timer1.Enabled = true;
        }

        protected void ddlUpdateFiled_SelectedIndexChanged(object sender, EventArgs e)
        {
            panUpdateButton.Visible = false;
        }

        protected void linkRejectColumn_Click(object sender, EventArgs e)
        {
            LinkButton link = (LinkButton)sender;
            if (link.Text.Equals("排除"))
            {
                ((TextBox)gdUpdateView.Rows[int.Parse(link.CommandName)].FindControl("txtDataUpdateValue")).Enabled = false;
                link.Text = "包含";
            }
            else
            {
                ((TextBox)gdUpdateView.Rows[int.Parse(link.CommandName)].FindControl("txtDataUpdateValue")).Enabled = true;
                link.Text = "排除";
            }
        }

        protected void gdUpdateView_DataBound(object sender, EventArgs e)
        {
            for (int i = 0; i < gdUpdateView.Rows.Count; i++)
            {
                if (!((TextBox)gdUpdateView.Rows[i].FindControl("txtDataUpdateValue")).Enabled)
                {
                    ((LinkButton)gdUpdateView.Rows[i].FindControl("linkRejectColumn")).Enabled = false;
                    ((LinkButton)gdUpdateView.Rows[i].FindControl("linkRejectColumn")).Text = "--";
                    ((TextBox)gdUpdateView.Rows[i].FindControl("txtDataUpdateValue")).Attributes.Add("onkeydown", "if(event.keyCode==13) {return false;}");
                }
            }
        }

        #endregion

        #region 设置界面操作
        protected void linkExit_Click(object sender, EventArgs e)
        {
            Session.RemoveAll();
            Response.Redirect("Default.aspx?url=exit");
        }

        protected void btnSetting_Click(object sender, EventArgs e)
        {
            if (!panSettings.Visible)
            {
                closeSelect();
                labHeaderName.Text = "设置";
                panSettings.Visible = true;
                linkExit.Visible = (Session["Server"] != null || Session["sConn"] != null && Session["mConn"] != null);
                linkBack.Visible = !linkExit.Visible;
                lnkDeleteDatabase.Visible = false;
                if (Session["Server"] != null)
                {
                    labServer.Text = Session["Server"].ToString();
                    if (Session["Database"] != null)
                    {
                        labDataBase.Text = Session["Database"].ToString();
                        if (!findAnyString(SYSTEM_DB,labDataBase.Text))
                            lnkDeleteDatabase.Visible = true;
                    }
                        
                    txtConnection.Text = Session["sConn"].ToString();
                }
            } 
        }

        protected void lnkDeleteDatabase_Click(object sender, EventArgs e)
        {
            getSconn();
            _bll = new ToolBll(Session["mConn"].ToString());
            _bll.deleteDatabase(labDataBase.Text);
            closeSelect();
            panMessage.Visible = 
            panTabControl.Visible = 
            panToolBar.Visible = false;
            showDatabase();
        }

        private void reConnected()
        {
            ConnectionStrings dlconn = new ConnectionStrings(txtConnection.Text.Trim('"'));
            Session["Server"] = dlconn.server;//2016.1.4 Changed
            Session["mConn"] = dlconn.OriginalConnectionString;
            if (!showDatabase())
            {
                labReConnect.Text = "连接失败，请重试！";
                imgReConnect.Visible = false;
                labServer.Text = dlconn.server;
                return;
            }
            labServer.Text = dlconn.server;
            int index = ExsitDatabase(ddlDatabase.Items, dlconn.database);
            if (index >= 0 && !dlconn.database.Equals("master"))
            {
                ddlDatabase.SelectedIndex = index;
                Session["Database"] = dlconn.database = ddlDatabase.SelectedValue;
                labDataBase.Text = Session["Database"].ToString();
                dlconn.correctConnectString();//2015.6.19 Changed
                Session["sConn"] = dlconn.connection;
                _bll = new ToolBll(dlconn.connection);
                lsbDataTable.Items.Clear();
                BindDataList(TYPE_TABLE);
                txtConnection.Text = dlconn.connection;
                //Changed to 2015.06.04
                panMessage.Visible = false;
                //closeSelect();
                lnkDelete.Visible = false;
                lnkDeleteDatabase.Visible = !findAnyString(SYSTEM_DB, labDataBase.Text);
            }
            else
                labReConnect.Text = "连接成功！";
            if (!linkExit.Visible && linkBack.Visible)
            {
                linkExit.Visible = true;
                linkBack.Visible = false;
            }
            imgReConnect.Visible = false;
        }

        protected void btnReConnect_Click(object sender, EventArgs e)
        {
            labReConnect.Text = "";
            if (txtConnection.Text.Trim().Length > 0)
            {
                panToolBar.Visible = panTabControl.Visible = false;
                imgReConnect.Visible = true;
                btnReConnect.Enabled = false;
                btnReConnect.Text = "正在连接";
                labServer.Text = "正在搜索…";
                labDataBase.Text = "";
                lnkDeleteDatabase.Visible = false;
                Timer2.Enabled = true;
            }
            else
                labReConnect.Text = "请输入连接字符串！";
        }

        protected void Timer2_Tick(object sender, EventArgs e)
        {
            reConnected();
            btnReConnect.Enabled = true;
            btnReConnect.Text = "重新连接";
            Timer2.Enabled = false;
        }
        #endregion

        #region 新增表界面
        protected void btnNewTable_Click(object sender, EventArgs e)
        {
            if ((!rdbNewProc.Checked && !rdbCustom.Checked) && txtNewTable_Name.Text.Trim().Length == 0)
            {
                labNewTableTip.Text = "请输入表名";
                Timer1.Enabled = true;
                return;
            }
            if (txtColumnList.Text.Trim().Length < 1)
            {
                labNewTableTip.Text = "请输入定义或SQL脚本";
                Timer1.Enabled = true;
                return;
            }
            getSconn();
             _bll = new ToolBll(Session["sConn"].ToString());
            labNewTableTip.Text = "正在执行";
            if (rdbNewTable.Checked)
            {
                if (_bll.createTable(txtNewTable_Name.Text.Trim(), txtColumnList.Text.Trim()))
                {
                    labNewTableTip.Text = "新增成功";
                    BindDataList(hidTabType.Value);
                    panToolBar.Visible = true;
                    panMessage.Visible = false;
                    lnkDelete.Visible = false;
                    txtNewTable_Name.Text = txtColumnList.Text = string.Empty;
                }
                else
                    labNewTableTip.Text = "新增失败，请检查输入是否有误";
            }
            else if (rdbNewView.Checked)
            {
                if (_bll.createView(txtNewTable_Name.Text.Trim(), txtColumnList.Text.Trim()))
                {
                    labNewTableTip.Text = "新增成功";
                    BindDataList(hidTabType.Value);
                    panToolBar.Visible = true;
                    panMessage.Visible = false;
                    lnkDelete.Visible = false;
                    txtNewTable_Name.Text = txtColumnList.Text = string.Empty;
                }
                else
                    labNewTableTip.Text = "新增失败，请检查输入是否有误";
            }
            else if (rdbNewProc.Checked || rdbCustom.Checked)
            {
                if (!txtColumnList.Text.StartsWith("create", true, CultureInfo.CurrentCulture))
                {
                    labNewTableTip.Text = "只能执行create语句";
                }
                else if (_bll.createProcedure(txtColumnList.Text))
                {
                    labNewTableTip.Text = "新增成功";
                    BindDataList(hidTabType.Value);
                    panToolBar.Visible = true;
                    panMessage.Visible = false;
                    lnkDelete.Visible = false;
                    txtNewTable_Name.Text = txtColumnList.Text = string.Empty;
                }
                else
                    labNewTableTip.Text = "执行失败，请检查输入是否有误";
            }
            Timer1.Enabled = true;
        }
        protected void btnNewTableClose_Click(object sender, EventArgs e)
        {
            closeSelect();
            txtNewTable_Name.Text = txtColumnList.Text = string.Empty;
        }
        protected void rdbNewTable_CheckedChanged(object sender, EventArgs e)
        {
            labNameTable_Name.Visible = txtNewTable_Name.Visible = true;
            txtNewTable_Name.Text = txtColumnList.Text = "";
            if (rdbNewTable.Checked)
            {
                labNameTable_Name.Text = "表名：";
                labColumnList.Text = "列定义：<br />（每一列之间用逗号分隔）";
            }
            else if (rdbNewView.Checked)
            {
                labNameTable_Name.Text = "名称：";
                labColumnList.Text = "定义：<br />（AS后部分）";
            }
            else if (rdbNewProc.Checked)
            {
                labNameTable_Name.Visible = txtNewTable_Name.Visible = false;
                labColumnList.Text = "脚本：";
                txtColumnList.Text = "create proc [name] @param [type] \nas\nbegin\n\n\nend";
            }
            else if (rdbCustom.Checked)
            {
                labNameTable_Name.Visible = txtNewTable_Name.Visible = false;
                labColumnList.Text = "脚本：";
            }
        }
        #endregion

        #region 存储过程参数界面
        string[]tmpProcParam;
        private List<DataProcedure> getParameter()
        {
            getSconn();
             _bll = new ToolBll(Session["sConn"].ToString());
            List<DataProcedure> _list = _bll.getProcedureParam(hidSchemasName.Value, hidTablesName.Value);
            hidProcExec.Value = "";
            if (_list.Count > 0)
            {
                string parast = "";
                tmpProcParam = new string[_list.Count];
                for (int i = 0; i < _list.Count; i++)
                    tmpProcParam[i] = _list[i].ParaName;
                foreach (string tmpStr in tmpProcParam)
                    parast += tmpStr + ",";
                hidProcExec.Value = parast.Trim(',');
            }
            return _list;
        }
        protected void btnProcparam_Click(object sender, EventArgs e)
        {
            if (!panProcParam.Visible)
            {
                closeSelect();
                labHeaderName.Text = "存储过程参数";
                panProcParam.Visible = true;
                gdProcpara.DataSource = getParameter();
                gdProcpara.DataBind();
            }
        }
        protected void btnProcexecute_Click(object sender, EventArgs e)
        {
            closeSelect();
            labHeaderName.Text = "执行存储过程";
            panProcExec.Visible = true;
            txtProcExec.Text = hidProcExec.Value;
        }
        #endregion

        #region 存储过程执行页面
        protected void btnProcAction_Click(object sender, EventArgs e)
        {
            if (!panProcExec.Visible)
            {
                closeSelect();
                getParameter();
                txtProcExec.Text = hidProcExec.Value;
                labHeaderName.Text = "执行存储过程";
                panProcExec.Visible = true;
            }
        }

        protected void btnProcExecAction_Click(object sender, EventArgs e)
        {
            getSconn();
             _bll = new ToolBll(Session["sConn"].ToString());
            DataSet ds;
            try
            {
                labProcExec.Text = "";
                if (hidProcExec.Value.Trim().Equals(""))
                    ds = _bll.execProc(getAbsoluteTableName(hidSchemasName.Value, hidTablesName.Value));
                else
                    ds = _bll.execProc(getAbsoluteTableName(hidSchemasName.Value, hidTablesName.Value), hidProcExec.Value.Split(','), txtProcExec.Text);
                if (ds != null)
                {
                    panProcExecData.Visible = true;
                    gdProcedureData.DataSource = ds;
                    gdProcedureData.DataBind();
                }
            }
            catch (Exception er)
            {
                panProcExecData.Visible = false;
                labProcExec.Text = er.Message;
            }
        }
        #endregion

        #region 存储过程定义界面
        protected void btnDefine_Click(object sender, EventArgs e)
        {
            if (!panProcDefine.Visible)
            {
                closeSelect();
                labHeaderName.Text = "存储过程定义";
                panProcDefine.Visible = true;
                getProcDefine();
            }
        }

        private void getProcDefine()
        {
            getSconn();
             _bll = new ToolBll(Session["sConn"].ToString());
            txtProcDefine.Text = _bll.getProcDefine(hidSchemasName.Value, hidTablesName.Value);
        }
        #endregion

    }
}