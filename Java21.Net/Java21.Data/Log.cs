using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.IO;
using System.Configuration;

namespace Java21.Data
{
    public class Log
    {
        private static string logpath = ConfigurationManager.AppSettings["logPath"].ToString(); 
        #region 内部核心方法
        private static string getDate()
        {
            DateTime date = DateTime.Now;
            return date.ToString("yyyyMMdd");
        }

        private static string getTime()
        {
            DateTime date = DateTime.Now;
            return date.ToString("HH:mm:ss");
        }

        private static string getTime(DateTime date)
        {
            return date.ToString("HH:mm:ss");
        }

        /// <summary>
        /// 写日志记录核心方法
        /// </summary>
        /// <param name="log">日志消息</param>
        /// <param name="path">文件路径</param>
        /// <param name="time">时间</param>
        /// <param name="apend">是否追加</param>
        /// <returns>状态返回值(成功true，失败false)</returns>
        private bool writeLog(string log, string path, string time, bool apend)
        {
            bool state = false;
            DirectoryInfo di = new DirectoryInfo(path.Substring(0, path.LastIndexOf("\\")));
            if (!di.Exists)
            {
                di.Create();
            }
            FileInfo fi = new FileInfo(path);
            if (!fi.Exists)
            {
                FileStream fs = fi.Open(FileMode.Create);
                fs.Close();
            }
            if (!fi.Exists)
            {
                return writeLog(log, path, time, apend);
            }
            StreamWriter sw = new StreamWriter(path,apend);
            try
            {
                sw.WriteLine("[" + time + "]\r\n\t" + log);
                state = true;
            }
            catch (IOException e)
            {
                throw new LogException(e.Message);
            }
            catch (Exception e)
            {
                throw new LogException(e.Message);
            }
            finally
            {
                sw.Close();
            }
            return state;
        } 
        #endregion

        #region 静态方法

        #region Log.i
        //Log.i
        public static bool i(string msg, DateTime time)
        {
            return new Log().writeLog(msg, logpath + getDate() + ".log", getTime(time), true);
        }

        public static bool i(string msg, DateTime time, bool apend)
        {
            return new Log().writeLog(msg, logpath + getDate() + ".log", getTime(time), apend);
        }

        public static bool i(string msg, string path, string time)
        {
            return new Log().writeLog(msg, path, time, true);
        }

        public static bool i(string msg, string path, string time, bool apend)
        {
            return new Log().writeLog(msg, path, time, apend);
        }

        public static bool i(string msg, string path, DateTime time)
        {
            return new Log().writeLog(msg, path, getTime(time), true);
        }

        public static bool i(string msg, string path, DateTime time, bool apend)
        {
            return new Log().writeLog(msg, path, getTime(time), apend);
        }

        public static bool i(string msg, string path)
        {
            return new Log().writeLog(msg, path, getTime(), true);
        }

        public static bool i(string msg, string path, bool apend)
        {
            return new Log().writeLog(msg, path, getTime(), apend);
        }

        /*public static bool i(string msg, string time, bool apend)
        {
            return new Log().writeLog(msg, new FileInfo(@"C:\Log\" + getDate() + ".log"), time, apend);
        }*/

        public static bool i(string msg, bool apend)
        {
            return new Log().writeLog(msg, logpath + getDate() + ".log", getTime(), apend);
        }

        public static bool i(string msg)
        {
            return new Log().writeLog(msg, logpath + getDate() + ".log", getTime(), true);
        } 
        #endregion

        #region Log.e
        //Log.e
        public static void e(string msg, string path, DateTime time)
        {
            new Log().writeLog(msg, path, getTime(time), true);
        }

        public static void e(string msg, string path, DateTime time, bool apend)
        {
            new Log().writeLog(msg, path, getTime(time), apend);
        }

        public static void e(string msg, DateTime time)
        {
            new Log().writeLog(msg, logpath + getDate() + ".log", getTime(time), true);
        }

        public static void e(string msg, DateTime time, bool apend)
        {
            new Log().writeLog(msg, logpath + getDate() + ".log", getTime(time), apend);
        }

        public static void e(string msg, string path, string time)
        {
            new Log().writeLog(msg, path, time, true);
        }

        public static void e(string msg, string path, string time, bool apend)
        {
            new Log().writeLog(msg, path, time, apend);
        }

        public static void e(string msg, string path)
        {
            new Log().writeLog(msg, path, getTime(), true);
        }

        public static void e(string msg, string path, bool apend)
        {
            new Log().writeLog(msg, path, getTime(), apend);
        }

        /*public static void e(string msg, string time, bool append)
        {
            new Log().writeLog(msg, new FileInfo(@"C:\Log\" + getDate() + ".log"), time, apend);
        }*/

        public static void e(string msg, bool apend)
        {
            new Log().writeLog(msg, logpath + getDate() + ".log", getTime(), apend);
        }

        public static void e(string msg)
        {
            new Log().writeLog(msg, logpath + getDate() + ".log", getTime(), true);
        }  
        #endregion

        #endregion
    }

    #region LogException
    public class LogException : Exception
    {
        public string message { get; set; }
        public LogException(){}
        public LogException(string message)
        {
            this.message = message;
        }
        public string getMessage()
        {
            return message;
        }
    } 
    #endregion
}
