using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Web.Routing;

namespace Java21.NET
{
    public class RouteConfig
    {
        public static void RegisterRoutes(RouteCollection routes)
        {
            routes.IgnoreRoute("{resource}.axd/{*pathInfo}");
            routes.IgnoreRoute("Views/Java21/index1.html");

            routes.MapRoute(
                name: "Java",
                url: "{action}",
                defaults: new { controller = "Java21", action = "Home" }
            );
            routes.MapRoute(
                name: "Users",
                url: "Users/{action}",
                defaults: new { controller = "Users", action = "Login" }
            );
            routes.MapRoute(
                name: "Artical",
                url: "article/{id}",
                defaults: new { controller = "article", action = "Views", id="1000"}
            );
           // routes.MapRoute(
           //    name: "Artical1",
           //    url: "article/{action}/{id}",
           //    defaults: new { controller = "article", action = "Views", id = "1000" }
           //);
            routes.MapRoute(
                name: "Java1",
                url: "Java21/{action}",
                defaults: new { controller = "Java21", action = "Home" }
            );
            routes.MapRoute(
                name: "Java2",
                url: "{action}/{id}",
                defaults: new { controller = "Java21", action = "Home", id = UrlParameter.Optional }
            );
            routes.MapRoute(
                name: "Default",
                url: "{controller}/{action}/{id}",
                defaults: new { controller = "Java21", action = "Home", id = UrlParameter.Optional }
            );
        }
    }
}