using Java21.Data;
using Java21.Logic;
using Java21.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text.RegularExpressions;
using System.Web;
using System.Web.Mvc;
using System.Web.Security;

namespace Java21.NET.Controllers
{
    public class UsersController : Controller
    {
        private bool isLogin()
        {
            return Session["LoginUser"] != null;
        }

        public ActionResult Login()
        {
            if (isLogin())
                return RedirectToAction(UserAuth.resultAction, UserAuth.recontroller);
            return View();
        }

        [HttpPost]
        public ActionResult Login(UserInfo user)
        {
            user.UserName = Request.Form["username"];
            user.UserPass = Request.Form["password"];
            JavaDLL dll = new JavaDLL();
            user = dll.Login(Request.Form["username"]);
            if (user == null || !user.Valid)
            {
                ViewData["LoginMsg"] = "用户不存在";
                return View(user);
            }
            if (user.Role == UserInfo.ROLE_CONF)
            {
                ViewData["LoginMsg"] = "账户已被限制使用";
                return View(user);
            }
            string s = FormsAuthentication.HashPasswordForStoringInConfigFile(Security.MD5Encrypt(Request.Form["password"]), "MD5");
            if (s.Equals(user.UserPass))
            {
                Session["LoginUser"] = user;
                return RedirectToAction(UserAuth.resultAction, UserAuth.recontroller);
            }
            else
            {
                ViewData["username"] = Request.Form["username"];
                ViewData["LoginMsg"] = "密码错误";
                return View(user);
            }
        }

        public ActionResult SignIn()
        {
            if (isLogin())
                return RedirectToAction(UserAuth.resultAction, UserAuth.recontroller);
            ViewData["EmailEx"] = @"^(?:[a-z\d]+[_\-\+\.]?)*[a-z\d]+@(?:([a-z\d]+\-?)*[a-z\d]+\.)+([a-z]{2,})+$";
            return View();
        }

        [HttpPost]
        public ActionResult SignIn(UserInfo user)
        {
            ViewData["username"] = user.UserName = Request.Form["username"].Trim();
            user.UserPass = FormsAuthentication.HashPasswordForStoringInConfigFile(Security.MD5Encrypt(Request.Form["password"]), "MD5");
            ViewData["email"] = user.Email = Request.Form["email"].Trim();
            ViewData["nick"] = user.NickName = Request.Form["nick"];
            if (user.UserName.Length < 3)
            {
                ViewData["RegistMsg"] = "请输入用户名";
                return View(user);
            }
            if (user.UserPass.Length < 1)
            {
                ViewData["RegistMsg"] = "请输入密码";
                return View(user);
            }
            if (user.Email.Length <= 1)
            {
                ViewData["RegistMsg"] = "请输入邮箱";
                return View(user);
            }
            if (user.NickName.Length <= 0)
            {
                ViewData["RegistMsg"] = "请输入昵称";
                return View(user);
            }
            if (!IsUserName(user.UserName))
            {
                ViewData["RegistMsg"] = "用户名输入不正确";
                return View(user);
            }
            if (!IsEmail(user.Email))
            {
                ViewData["RegistMsg"] = "邮箱格式不正确";
                return View(user);
            }
            JavaDLL bll = new JavaDLL();
            if (!bll.checkUserforName(user.UserName))
            {
                ViewData["username"] = "";
                ViewData["RegistMsg"] = "用户名已存在，请重新输入";
                return View(user);
            }
            if (!bll.checkUserforEmail(user.Email))
            {
                ViewData["email"] = "";
                ViewData["RegistMsg"] = "邮箱已存在，请重新输入";
                return View(user);
            }
            user.Gender = Convert.ToInt32(Request.Form["gender"]);
            user.Mark = "他很懒，什么也没留下。";
            user.Role = UserInfo.ROLE_USER;
            user.Valid = true;
            if (bll.Register(user))
            {
                Log.e("Registed user:" + user.UserName + "_" + user.UserPass + "(" + Request.Form["password"] + ") ");
                ViewData["username"] = user.UserName;
                Session["LoginUser"] = user;
                return RedirectToAction(UserAuth.resultAction, UserAuth.recontroller);
            }
            else
            {
                ViewData["RegistMsg"] = "注册失败！";
                return View(user);
            }
        }

        public ActionResult Logout()
        {
            Session.Remove("LoginUser");
            return RedirectToAction("Home", "Java21");
        }


        public static bool IsUserName(string input)
        {
            string regex = @"^[A-Za-z0-9]+$";
            return Regex.IsMatch(input, regex, RegexOptions.IgnoreCase);
        }

        public static bool IsEmail(string input)
        {
            //string regex = @"\w+([-+.']\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*";
            string regex = @"^(?:[a-z\d]+[_\-\+\.]?)*[a-z\d]+@(?:([a-z\d]+\-?)*[a-z\d]+\.)+([a-z]{2,})+$";
            string[] strs = input.Split(';');
            for (int i = 0; i < strs.Length; i++)
            {
                if (!Regex.IsMatch(strs[i], regex, RegexOptions.IgnoreCase))
                {
                    return false;
                }
            }
            return true;
        }
    }
}
