using Java21.Logic;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace Java21.NET.Controllers
{
    public class articleController : Controller
    {
        public ActionResult Views(string id)
        {
            if (id.ToLower().Equals("views"))
                return RedirectToAction("article","Java21");
            JavaDLL dll = new JavaDLL();
            Model.Article mod = dll.getArticle(Convert.ToInt32(id.Split('.')[0]));
            return View(mod);
        }
    }
}
