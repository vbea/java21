using Java21.Logic;
using Java21.Model;
using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Text.RegularExpressions;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace Java21.Net
{
    public partial class JavaKnowledge : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                bool state = false;
                if (Request.QueryString["id"] != null)
                {
                    JavaDLL bll = new JavaDLL();
                    if (Session["LoginUser"] == null)
                    {
                        if (Request.QueryString["uid"] != null && Request.QueryString["sid"] != null)
                        {
                            UserInfo info = bll.Login(Request.QueryString["uid"]);
                            if (!info.UserPass.Equals(Request.QueryString["sid"]))
                                labError.Text = "你需要登录后才能发表";
                            else
                            {
                                Session["LoginUser"] = info;
                                imgPhone.Visible = state = true;
                            }
                        }
                        else
                            labError.Text = "你需要登录后才能发表";
                    }
                    else
                        state = true;
                    if (getData(bll, Convert.ToInt32(Request.QueryString["id"])) && state)
                        btnComment.Enabled = true;
                }
                else
                    Response.End();
            }
        }

        private bool getData(JavaDLL bll, int id)
        {
            artical.InnerHtml = "<span class='gray'>文章不存在或已被删除.</span>";
            DataSet ds = bll.getArticle(id, true);
            if (ds == null || ds.Tables[0].Rows.Count == 0)
                return false;
            DataRow row = ds.Tables[0].Rows[0];
            if (row == null)
                return false;
            labTitle.Text = litTitle.Text = row["title"].ToString();
            litTitle.Text += "-Java知识点";
            if (!Convert.ToBoolean(row["valid"]))
                return false;
            artical.InnerHtml = row["artical"].ToString();
            if (row["euser"].Equals(DBNull.Value))
                writer.InnerHtml = "<span class='gray'>" + row["cuser"] + "</span> 创建于" + Convert.ToDateTime(row["cdate"]).ToString("M月dd日 HH:mm");
            else
                writer.InnerHtml = "<span class='gray'>" + row["euser"] + "</span> 修改于" + Convert.ToDateTime(row["edate"]).ToString("M月dd日 HH:mm");
            DataSet comst = bll.getComment(id);
            litStatistics.Text = row["cread"].ToString();
            litStatisticz.Text = "" + comst.Tables[0].Rows.Count;
            gvComment.DataSource = comst;
            gvComment.DataBind();
            return true;
        }

        private void getComment()
        {
            JavaDLL dll = new JavaDLL();
            gvComment.DataSource = dll.getComment(Convert.ToInt32(Request.QueryString["id"]));
            gvComment.DataBind();
        }

        public string show_content(string str)
        {
            str = Regex.Replace(str, @"\<", "&lt;");
            str = Regex.Replace(str, @"\>", "&gt;");
            str = Regex.Replace(str, @"\n", "<br/>");
            str = str.Replace(" ", "&nbsp;");
            str = Regex.Replace(str, @"\[em_([0-9]*)\]", "<img src='/images/arclist/$1.gif' border='0' />");
            return str;
        }

        public string getStar(object count, object device)
        {
            int st = Convert.ToInt32(count);
            string result = "";
            for (int i = 0; i < 5; i++)
            {
                if (i < st)
                    result += "<img src='/images/star_light.png'/>";
                else
                    result += "<img src='/images/star_normal.png'/>";
            }
            if (!device.Equals(DBNull.Value))
                result += "<br /><span class='gray2'>" + device + "</span>";
            return result;
        }

        protected void btnComment_Click(object sender, EventArgs e)
        {
            if (Session["LoginUser"] == null)
            {
                labError.Text = "你需要登录后才能发表";
                return;
            }
            labError.Text = "";
            if (hidStart.Value.Equals("0"))
            {
                labError.Text = "请点击星星评分";
                hidStart.Value = "0";
                return;
            }
            if (txtComment.Text.Trim().Length == 0)
            {
                labError.Text = "请输入评论内容";
                hidStart.Value = "0";
                return;
            }
            JavaDLL dll = new JavaDLL();
            if (Request.QueryString["device"] == null)
            {
                if (dll.addComment(Convert.ToInt32(Request.QueryString["id"]), ((UserInfo)Session["LoginUser"]).UserName, Convert.ToInt32(hidStart.Value), txtComment.Text.Trim()))
                {
                    txtComment.Text = "";
                    hidStart.Value = "0";
                    getComment();
                }
                else
                    labError.Text = "评论发送失败";
            }
            else
            {
                if (dll.addComment(Convert.ToInt32(Request.QueryString["id"]), ((UserInfo)Session["LoginUser"]).UserName, Convert.ToInt32(hidStart.Value), txtComment.Text.Trim(), Request.QueryString["device"]))
                {
                    txtComment.Text = "";
                    hidStart.Value = "0";
                    getComment();
                }
                else
                    labError.Text = "评论发送失败";
            }
        }

        protected void gvComment_PageIndexChanging(object sender, GridViewPageEventArgs e)
        {
            gvComment.PageIndex = e.NewPageIndex;
            getComment();
        }

    }
}