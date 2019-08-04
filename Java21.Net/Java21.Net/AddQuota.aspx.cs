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
    public partial class AddQuota : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                if (Request.QueryString["action"] != null && Session["LoginUser"] != null)
                {
                    if (((UserInfo)Session["LoginUser"]).Role == UserInfo.ROLE_ADMIN)
                    {
                        if (Request.QueryString["action"].Equals("add"))
                            btnAdd.Visible = true;
                        else if (Request.QueryString["action"].Equals("edt") && Request.QueryString["id"] != null)
                        {
                            getData(Convert.ToInt32(Request.QueryString["id"]));
                            btnEdit.Visible =
                            chkDate.Visible = true;
                        }
                    }
                }
                else
                    Response.Redirect("Login.aspx");
            }
        }

        private void getData(int id)
        {
            JavaDLL dll = new JavaDLL();
            DataSet ds = dll.getQuotation(id);
            if (ds != null)
            {
                DataRow row = ds.Tables[0].Rows[0];
                if (row == null)
                    return;
                txtValues.Text = row["sentence"].ToString();
                chkTip.Checked = Convert.ToBoolean(row["tips"]);
                btnEdit.CommandName = row["cdate"].ToString();
            }
        }

        protected void btnAdd_Click(object sender, EventArgs e)
        {
            if (txtValues.Text.Trim().Equals(""))
                ClientScript.RegisterStartupScript(GetType(), "err", "alert('请输入内容');", true);
            else
            {
                JavaDLL dll = new JavaDLL();
                if (dll.addQuotations(txtValues.Text.Trim(), chkTip.Checked))
                    ClientScript.RegisterStartupScript(GetType(), "err", "top.closeDialog()", true);
                else
                    ClientScript.RegisterStartupScript(GetType(), "err", "alert('添加失败！');", true);
            }
        }

        protected void btnEdit_Click(object sender, EventArgs e)
        {
            if (txtValues.Text.Trim().Equals(""))
                ClientScript.RegisterStartupScript(GetType(), "err", "alert('请输入内容');", true);
            else
            {
                JavaDLL dll = new JavaDLL();
                if (dll.updateQuotations(Convert.ToInt32(Request.QueryString["id"]), txtValues.Text.Trim(), chkTip.Checked, chkDate.Checked ? DateTime.Now : Convert.ToDateTime(btnEdit.CommandName)))
                    ClientScript.RegisterStartupScript(GetType(), "err", "top.closeDialog()", true);
                else
                    ClientScript.RegisterStartupScript(GetType(), "err", "alert('修改失败！');", true);
            }
        }

    }
}