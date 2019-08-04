using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Configuration;
using Java21.Data;
using Java21.Model;
using System.Data;

namespace Java21.Logic
{
    public class JavaDLL
    {
        private JavaDAL dal;
        public JavaDLL()
        {
            dal = new JavaDAL(ConfigurationManager.ConnectionStrings["conn"].ConnectionString);
        }

        #region 用户表数据操作
        public DataSet getAllUser()
        {
            return dal.getAllUser();
        }

        public UserInfo Login(string user)
        {
            return dal.Login(user);
        }

        public UserInfo Login(int userid)
        {
            return dal.Login(userid);
        }

        public bool Register(UserInfo info)
        {
            return dal.Register(info) > 0;
        }

        public bool checkUserforName(string name)
        {
            return (Convert.ToInt32(dal.checkUserforName(name)) == 0);
        }

        public bool checkUserforEmail(string email)
        {
            return (Convert.ToInt32(dal.checkUserforEmail(email)) == 0);
        }

        public bool updateUser(UserInfo info)
        {
            return dal.updateUser(info) > 0;
        }

        public bool updateFillUser(UserInfo info)
        {
            return dal.updateFillUser(info) > 0;
        }

        public bool changePassword(int id, string password)
        {
            return dal.changePassword(id, password) > 0;
        }

        public bool deleteUser(int id)
        {
            return dal.deleteUser(id) > 0;
        }
        #endregion

        #region 注册码数据操作
        public DataSet getAllKeys()
        {
            return dal.getAllKeys();
        }

        public DataSet getKey(string key)
        {
            return dal.getKey(key);
        }

        public DataSet getKey()
        {
            return dal.getKey();
        }

        public bool addKeys(string key, int max, string ver, string mark)
        {
            if (Convert.ToInt32(dal.getKeyCount(key)) == 0)
                return dal.addKeys(key, max, ver, mark) > 0;
            else
                return false;
        }

        public int getKeyCount(string key)
        {
            return Convert.ToInt32(dal.getKeyCount(key));
        }

        public bool registKey(string key)
        {
            return dal.registKey(key) > 0;
        } 

        public bool deleteKey(int id)
        {
            return dal.deleteKey(id) > 0;
        }
        #endregion

        #region 视频表数据操作
        public DataSet getAllVideo()
        {
            return dal.getAllVideo();
        }

        public string getVideoUrl(string id)
        {
            return dal.getVideoUrl(id).ToString();
        } 
        #endregion

        #region 语录/公告表数据操作
        public DataSet getQuotations()
        {
            return dal.getQuotations();
        }

        public DataSet getAllQuotations()
        {
            return dal.getAllQuotations();
        }

        public DataSet getQuotation(int id)
        {
            return dal.getQuotation(id);
        }

        public bool addQuotations(string value, bool tips)
        {
            return dal.addQuotations(value, tips) > 0;
        }

        public bool updateQuotations(int id, string value, bool tips, DateTime cdate)
        {
            return dal.updateQuotations(id, value, tips, cdate) > 0;
        }

        public bool delQuotations(int id)
        {
            return dal.delQuotations(id) > 0;
        } 
        #endregion

        #region 知识点表数据操作
        public DataSet getKnowledge()
        {
            return dal.getKnowledge();
        }

        public DataSet getKnowledge(int count, int pageindex)
        {
            return dal.getKnowledge(count, pageindex);
        }

        public DataSet getKnowledge(int id, bool read)
        {
            if (read)
                dal.addKnowledgeRead(id);
            return dal.getKnowledge(id);
        }

        public DataSet getRecycleKnowledge()
        {
            return dal.getRecycleKnowledge();
        }

        public bool addKnowledge(string title, string artical, string user)
        {
            return dal.addKnowledge(title, artical, user) > 0;
        }


        public bool updateKnowledge(int id, string title, string artical, string user)
        {
            return dal.updateKnowledge(id, title, artical, user) > 0;
        }

        public bool updateKnowledge(int id, bool valid)
        {
            return dal.updateKnowledge(id, valid) > 0;
        }

        public bool deleteKnowledge(int id)
        {
            return dal.updateKnowledge(id, false) > 0;
        }

        public bool restoreKnowledge(int id)
        {
            return dal.updateKnowledge(id, true) > 0;
        } 

        public bool delKnowledge(int id)
        {
            return dal.deleteKnowledge(id) > 0;
        }

        public bool clearKnowledge()
        {
            return dal.clearKnowledge() > 0;
        }
        #endregion

        #region 评论表数据操作
        public bool addComment(int aid, string uid, int star, string comment)
        {
            dal.addKnowledgeComment(aid);
            return dal.addComment(aid, uid, star, comment) > 0;
        }

        public bool addComment(int aid, string uid, int star, string comment, string device)
        {
            dal.addKnowledgeComment(aid);
            return dal.addComment(aid, uid, star, comment, device) > 0;
        }

        public DataSet getComment(int aid)
        {
            return dal.getComment(aid);
        }

        public bool deleteComment(int id)
        {
            return dal.deleteComment(id) > 0;
        } 
        #endregion

        #region 反馈表数据操作
        public DataSet getFeedback()
        {
            return dal.getFeedback();
        }
        public bool addFeedback(string feed, string contact, string device)
        {
            return dal.addFeedback(feed, device, contact) > 0;
        }

        public bool delFeedback(int id)
        {
            return dal.delFeedback(id) > 0;
        } 
        #endregion
    }
}
