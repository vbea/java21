using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using ToolBLL;

namespace Database_Tools
{
    public partial class Default : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
                showValidate();
        }

        protected void btnConnect_Click(object sender, EventArgs e)
        {
            if (txtServerName.Text.Trim().Length < 1)
            {
                Common.regesterAlertScript(this, "ser", "请输入服务器名!");
                txtServerName.Focus();
            }
            else if (txtUserName.Text.Trim().Length < 1 && rdbSql.Checked)
            {
                Common.regesterScript(this, "usr", "alert('请输入用户名！');");
                txtUserName.Focus();
            }
            else
            {
                if (Convert.ToInt32(Session["LinkID"]) != -2)
                {
                    if (rdbSql.Checked)
                        Session["mConn"] = "Server=" + txtServerName.Text.Trim() + ";Database=master; UID=" + txtUserName.Text + "; PWD=" + txtPsd.Text + "; Connection Timeout=4500;Pooling=True;";
                    else
                        Session["mConn"] = "Data Source=" + txtServerName.Text.Trim() + ";Initial Catalog=master;Integrated Security=True";
                    if (txtServerName.Text.Trim().Equals("."))
                        Session["Server"] = "localhost";
                    else
                        Session["Server"] = txtServerName.Text;
                    Session["sConn"] = Session["mConn"];
                    Session["LinkID"] = 0;
                    Session["Database"] = "master";
                    Response.Redirect("HomePage.aspx?id=" + Session.SessionID);
                }
                else
                {
                    Common.regesterScript(this, "err", "alert('无法连接服务器，请测试后再试');");
                }
            }
        }

        protected void btnTest_Click(object sender, EventArgs e)
        {
            if (txtServerName.Text.Trim().Length < 1)
            {
                Common.regesterScript(this, "ser", "alert('请输入服务器名！');");
                txtServerName.Focus();
            }
            else if (txtUserName.Text.Trim().Length < 1 && rdbSql.Checked)
            {
                Common.regesterScript(this, "usr", "alert('请输入用户名！');");
                txtUserName.Focus();
            }
            else
            {
                ToolBll bll;
                string sql = "Data Source=" + txtServerName.Text + ";Initial Catalog=master;Integrated Security=True";
                string conn = "Server=" + txtServerName.Text + ";Database=master; UID=" + txtUserName.Text + "; PWD=" + txtPsd.Text + "; Connection Timeout=4500;Pooling=True;";
                if (rdbSql.Checked)
                {
                    bll = new ToolBll(conn);
                    Session["mConn"] = conn;
                }
                else
                {
                    bll = new ToolBll(sql);
                    Session["mConn"] = sql;
                }
                if (bll.TestConnect())
                {
                    Common.regesterScript(this, "test", "alert('连接成功！');");
                    Session["LinkID"] = 1;
                }
                else
                {
                    Common.regesterScript(this, "loos", "alert('连接失败！');");
                    Session["LinkID"] = -2;
                }
            }
        }

        protected void rdbValidate_CheckedChanged(object sender, EventArgs e)
        {
            showValidate();
        }


        private void showValidate()
        {
            palLogin.Visible = !rdbWin.Checked;
            Session["LinkID"] = 0;
        }

        protected void imbGoto_Click(object sender, ImageClickEventArgs e)
        {
            Response.Redirect("HomePage.aspx");
        }
    }
}