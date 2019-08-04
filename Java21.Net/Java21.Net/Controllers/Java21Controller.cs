using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Java21.NET.Models;
using Java21.Model;
using Java21.Logic;
using Webdiyer.WebControls.Mvc;

namespace Java21.NET.Controllers
{
    public class Java21Controller : Controller
    {
        //
        // GET: /Java21/
        /// <summary>
        /// 验证是否登录
        /// </summary>
        /// <returns></returns>
        private bool isLogin()
        {
            return Session["LoginUser"] != null;
        }

        public ActionResult Home()
        {
            return View();
        }

        public ActionResult Users()
        {
            if (isLogin())
                return View();
            else
                return Redirect("/Users/Login");
        }

        public ActionResult Download()
        {
            return View();
        }

        public ActionResult JavaKeys()
        {
            return View();
        }

        public ActionResult Article(int? p)
        {
            int pageindex = p ?? 1;
            int pagesize = 10;
            int total = 0;
            JavaDLL dll = new JavaDLL();
            PagedList<Model.Article> page = dll.getArticle(pagesize, pageindex, out total).AsQueryable().ToPagedList(pageindex, pagesize);
            page.TotalItemCount = total;
            page.CurrentPageIndex = pageindex;
            return View(page);
        }

        public ActionResult Video()
        {
            JavaDLL dll = new JavaDLL();
            List<Model.Video> video = dll.getAllVideos();
            return View(video);
        }

        public ActionResult About(string url)
        {
            return View();
        }

    }
}
