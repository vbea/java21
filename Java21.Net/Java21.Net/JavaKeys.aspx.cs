using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using Java21.Logic;
using Java21.Model;
using System.Data;

namespace Java21.Net
{
    public partial class JavaKeys : System.Web.UI.Page
    {
        public int role
        {
            get
            {
                return Convert.ToInt32(ViewState["role"]);
            }
            set
            {
                ViewState["role"] = value;
            }
        }
        protected void Page_Load(object sender, EventArgs e)
        {
            if (!IsPostBack)
            {
                UserInfo info = (UserInfo)Session["LoginUser"];
                if (info == null)
                    return;
                role = info.Role;
                getData();
                //string url = "http://" + Request.Url.Host + ResolveUrl("~/Java21web.asmx");
                //litTip1.Text = "1.手机访问注册码信息的URL为：" + url + "/GetKeyInfo?key=你的注册码<br />";
                //if (panTip3.Visible = role == UserInfo.ROLE_ADMIN)
                //    litTip1.Text += "2.客户端注册APP的URL为：" + url + "/RegistKey?key=你的注册码";
            }
        }

        private void getData()
        {
            JavaDLL dll = new JavaDLL();
            DataSet ds = dll.getAllKeys();
            gvKey.ShowFooter = (ds.Tables[0].Rows.Count <= gvKey.PageSize);
            gvKey.DataSource = ds;
            gvKey.DataBind();
        }

        public string getVersion(object ver)
        {
            switch (ver.ToString())
            {
                case "p":
                    return "专业版";
                default:
                    return "标准版";
            }
        }

        protected string showKeys(string key)
        {
            if (role == UserInfo.ROLE_ADMIN)
                return key;
            string show = "";
            if (key.Length > 20)
            {
                show += key.Substring(0, 5);
                show += showStar(key.Length - 10);
                show += key.Substring(key.Length - 5);
            }
            else if (key.Length <= 20 && key.Length > 7)
            {
                show += key.Substring(0, 2);
                show += showStar(key.Length - 4);
                show += key.Substring(key.Length - 2);
            }
            else if (key.Length <= 7 && key.Length > 4)
            {
                show += key.Substring(0, 1);
                show += showStar(key.Length - 2);
                show += key.Substring(key.Length - 1);
            }
            else if (key.Length <= 4)
            {
                show += key.Substring(0, 1);
                show += showStar(key.Length - 1);
            }
            return show;
        }

        private string showStar(int i)
        {
            int j = 0;
            string s = "";
            while (j < i)
            {
                s += "*";
                j++;
            }
            return s;
        }

        protected void lnkDelete_Click(object sender, EventArgs e)
        {
            int id = Convert.ToInt32((sender as LinkButton).CommandName);
            JavaDLL bll = new JavaDLL();
            if (bll.deleteKey(id))
                getData();
            else
                ClientScript.RegisterStartupScript(GetType(), "err", "alert('删除失败！');", true);
        }

        protected void gvKey_PageIndexChanging(object sender, GridViewPageEventArgs e)
        {
            gvKey.PageIndex = e.NewPageIndex;
            getData();
        }
    }
}