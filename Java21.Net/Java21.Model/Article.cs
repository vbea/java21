using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Java21.Model
{
    public class Article
    {
        public int id { get; set; }
        public string title { get; set; }
        public int category { get; set; }
        public string artical { get; set; }
        public Nullable<System.DateTime> cdate { get; set; }
        public string cuser { get; set; }
        public Nullable<System.DateTime> edate { get; set; }
        public string euser { get; set; }
        public Nullable<System.DateTime> comment { get; set; }
        public int cread { get; set; }
        public bool valid { get; set; }
    }
}
