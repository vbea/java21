using Java21.Logic;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace Java21.Net
{
    public partial class JavaVideo : System.Web.UI.Page
    {
        string url = "<embed src='{0}' quality='high' width='480' height='320' align='middle'  type='application/x-shockwave-flash' />";
        string[] asymble = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
        protected void Page_Load(object sender, EventArgs e)
        {
            /*if (!IsPostBack)
            {
                if (Request.QueryString["id"] != null)
                {
                    JavaDLL bll = new JavaDLL();
                    try
                    {
                        string u = bll.getVideoUrl(Request.QueryString["id"]);
                        javaVideo.InnerHtml = string.Format(url, u);
                    }
                    catch (Exception)
                    {
                        javaVideo.InnerHtml = "<div style=\"width:100%; text-align:left;\">Bad Request</div>";
                    }
                }
                else
                    javaVideo.InnerHtml = "<div style=\"width:100%; text-align:left;\">Bad Request</div>";
            }*/
        }

        protected void btnEncode_Click(object sender, EventArgs e)
        {
            string text = txtPiano.Text.Trim().Replace("\r","").Replace("\n","").Replace(" ","");
            int length = text.Length;
            string[] codes = new string[length];
            bool bracket = false;
            for (int i=0;i<length;i++)
            {
                string s = text.Substring(i, 1);
                if (s.Equals("("))
                {
                    bracket = true;
                    codes[i] = "(";
                    continue;
                }
                if (s.Equals(")"))
                {
                    bracket = false;
                    codes[i] = "),";
                    codes[i - 1] = codes[i - 1].Replace("-", "");
                    continue;
                }
                if (s.Equals("-"))
                {
                    codes[i] = "-1" + ",";
                    continue;
                }
                else
                {
                    if (bracket)
                        codes[i] = getIndex(s) + "-";
                    else
                        codes[i] = getIndex(s) + ",";
                }

            }
            txtValue.Text = string.Join("", codes).Trim(',').Replace("(", "").Replace(")", "");
        }

        private string getIndex(string asb)
        {
            for (int i=0;i<asymble.Length;i++)
            {
                if (asb.ToUpper().Equals(asymble[i]))
                    return ""+i;
            }
            return asb;
        }

        private string getString(string[] index)
        {
            string str = "";
            foreach (string q in index)
            {
                str += getString(q);
            }
            return str;
        }

        private string getString(string index)
        {
            try
            {
                return asymble[Convert.ToInt32(index)];
            }
            catch
            {
                return index;
            }
        }

        protected void btnEycode_Click(object sender, EventArgs e)
        {
            string[] text = txtPiano.Text.Trim().Split(new char[]{','},StringSplitOptions.RemoveEmptyEntries);
            string value = "";
            for (int i = 0; i < text.Length; i++)
            {
                string sd = text[i];
                if (sd.Equals("-1"))
                {
                    value += "-";
                }
                else if (sd.Length > 2)
                {
                    string[] li = sd.Split('-');
                    if (li.Length > 1)
                    {
                        value += "(" + getString(li) + ")";
                    }
                }
                else
                    value += getString(sd);
            }
            txtValue.Text = value;
        }
    }
}