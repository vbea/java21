using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;
using Java21.Logic;
using System.Data;
using Newtonsoft.Json.Linq;
using Java21.Model;
using System.Web.Security;
using Java21.Data;
using System.Configuration;

namespace Java21.Net
{
    /// <summary>
    /// Java21web 的摘要说明
    /// </summary>
    [WebService(Namespace = "http://vbes.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // 若要允许使用 ASP.NET AJAX 从脚本中调用此 Web 服务，请取消注释以下行。 
    [System.Web.Script.Services.ScriptService]
    public class Java21web : System.Web.Services.WebService
    {
        [WebMethod(Description = "创建一个注册码")]
        public string CreateKey(string key, string version, string max, string mark, string password)
        {
            JavaDLL dll = new JavaDLL();
            if (dll.Login("admin").UserPass.Equals(password))
            {
                return dll.addKeys(key, Convert.ToInt32(max), version, mark).ToString();
            }
            else
                return "Password error";
        }

        [WebMethod(Description = "获取指定注册码的信息")]
        public string GetKeyInfo(string key)
        {
            //Context.Response.ContentType = "text/plain";
            JavaDLL dll = new JavaDLL();
            DataSet ds = dll.getKey(key);
            if (ds.Tables[0].Rows.Count == 1)
            {
                DataRow row = ds.Tables[0].Rows[0];
                var keyvalue = new
                {
                    keys = new
                    {
                        key = row["keys"],
                        password = "java21",
                        time = (Convert.ToInt32(row["maxc"]) - Convert.ToInt32(row["curr"])),
                        version = row["ver"],
                        date = Convert.ToDateTime(row["cdate"]).ToString("yyyy-MM-dd"),
                        option = row["mark"],
                        response = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")
                    }
                };
                JObject keyjson = JObject.FromObject(keyvalue);
                return keyjson.ToString();
            }
            else
                return "No keys";
        }

        [WebMethod(Description = "提交一个注册码进行注册")]
        public bool RegistKey(string key)
        {
            return new JavaDLL().registKey(key);
        }

        [WebMethod(Description = "获取一个免费的注册码")]
        public string GetKey()
        {
            JavaDLL dll = new JavaDLL();
            DataSet ds = dll.getKey();
            if (ds != null && ds.Tables[0].Rows.Count > 0)
                return ds.Tables[0].Rows[0][0].ToString();
            else
                return "No keys";
        }

        [WebMethod(Description = "获取公告信息")]
        public string GetQuotations()
        {
            JavaDLL dll = new JavaDLL();
            DataSet ds = dll.getQuotations();
            int count = ds.Tables[0].Rows.Count;
            if (count > 0)
            {
                Random rand = new Random();
                return ds.Tables[0].Rows[rand.Next(0, count)]["sentence"].ToString();
            }
            else
                return "-";
        }

        [WebMethod]
        public string RegisterUser(string name, string psd, string nickname, int gender, string email)
        {
            UserInfo info = new UserInfo();
            info.UserName = name;
            info.UserPass = FormsAuthentication.HashPasswordForStoringInConfigFile(Security.MD5Encrypt(psd), "MD5");
            info.Role = UserInfo.ROLE_USER;
            info.Valid = true;
            info.NickName = nickname;
            info.Email = email;
            info.Gender = gender;
            info.Mark = "他很懒，什么也没留下";
            JavaDLL dll = new JavaDLL();
            if (dll.checkUserforName(info.UserName))
            {
                if (dll.checkUserforEmail(info.Email))
                {
                    if (dll.Register(info))
                        return "true";
                    else
                        return "注册失败，请重试";
                }
                else
                    return "该邮箱已经存在";
            }
            else
                return "该用户名已经存在";
        }

        [WebMethod]
        public string Login(string name, string sid, bool auto)
        {
            string pasd = auto ? sid : FormsAuthentication.HashPasswordForStoringInConfigFile(Security.MD5Encrypt(sid), "MD5");
            JavaDLL bll = new JavaDLL();
            UserInfo info = bll.Login(name);
            if (info != null)
            {
                if (info.UserPass.Equals(pasd))
                {
                    var json = new
                    {
                        user = info.UserName,
                        name = info.NickName,
                        sid = info.UserPass,
                        gender = info.Gender,
                        email = info.Email,
                        mark = info.Mark
                    };
                    JObject keyjson = JObject.FromObject(json);
                    string date = DateTime.Now.ToString("yyyyMMdd");
                    Log.e("Login:" + info.UserName, ConfigurationManager.AppSettings["logPath"] + "\\" + date + "\\user_" + date + ".log");
                    return keyjson.ToString();
                }
                else
                    return "true";
            }
            else
                return "false";
        }

        [WebMethod(Description = "获取知识点列表")]
        public string GetKnowledge(int page)
        {
            if (page > 0)
            {
                JavaDLL bll = new JavaDLL();
                DataSet ds = bll.getArticle(20, page);
                var json = new
                {
                    page = page,
                    count = ds.Tables[0].Rows.Count,
                    list = new List<object>()
                };
                foreach (DataRow row in ds.Tables[0].Rows)
                {
                    var _row = new
                    {
                        id = row["id"],
                        title = row["title"],
                        date = Convert.ToDateTime(row["edate"]).ToString("yyyy-MM-dd HH:mm"),
                        read = row["cread"]
                    };
                    json.list.Add(_row);
                }
                JObject keyjson = JObject.FromObject(json);
                return keyjson.ToString();
            }
            else
                return "null";
        }

        [WebMethod(Description = "提交反馈")]
        public bool Feedback(string suggest, string contact, string device)
        {
            JavaDLL bll = new JavaDLL();
            return bll.addFeedback(suggest, contact, device);
        }
    }
}
