using System.Web;
using System.Web.Mvc;

namespace Java21.NET
{
    public class FilterConfig
    {
        public static void RegisterGlobalFilters(GlobalFilterCollection filters)
        {
            filters.Add(new UserAuth());
            filters.Add(new HandleErrorAttribute());
        }
    }
}