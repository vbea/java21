using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Data;
using System.Data.SqlClient;
using Java21.Model;
using ToolDAL;

namespace Java21.Data
{
    public class JavaDAL
    {
        public string conn;
        public JavaDAL(string connect)
        {
            conn = connect;
        }

        #region 用户表数据操作
        public DataSet getAllUser()
        {
            string sql = "select * from Users";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            try
            {
                con.Open();
                SqlDataAdapter sda = new SqlDataAdapter(cmd);
                DataSet ds = new DataSet();
                sda.Fill(ds);
                return ds;
            }
            catch (Exception)
            {
                throw;
            }
            finally
            {
                con.Close();
            }
        }
        public UserInfo Login(string user)
        {
            string sql = "select * from Users where name=@name or email=@name";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter para = new SqlParameter("@name", user);
            cmd.Parameters.Add(para);
            try
            {
                con.Open();
                SqlDataReader read = cmd.ExecuteReader();
                if (read.Read())
                {
                    UserInfo info = new UserInfo();
                    info.ID = Convert.ToInt32(read["id"]);
                    info.UserName = read["name"].ToString();
                    info.UserPass = read["psd"].ToString();
                    info.Gender = Convert.ToInt32(read["gender"]);
                    info.NickName = read["nickname"].ToString();
                    info.Email = read["email"].ToString();
                    info.Valid = Convert.ToBoolean(read["valid"]);
                    info.Role = Convert.ToInt32(read["roles"]);
                    info.Mark = "" + read["mark"];
                    return info;
                }
                return null;
            }
            catch (Exception)
            {
                throw;
            }
            finally
            {
                con.Close();
            }
        }

        public UserInfo Login(int userid)
        {
            string sql = "select * from Users where id=@id";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter para = new SqlParameter("@id", userid);
            cmd.Parameters.Add(para);
            try
            {
                con.Open();
                SqlDataReader read = cmd.ExecuteReader();
                if (read.Read())
                {
                    UserInfo info = new UserInfo();
                    info.ID = Convert.ToInt32(read["id"]);
                    info.UserName = read["name"].ToString();
                    info.UserPass = read["psd"].ToString();
                    info.Gender = Convert.ToInt32(read["gender"]);
                    info.NickName = read["nickname"].ToString();
                    info.Email = read["email"].ToString();
                    info.Valid = Convert.ToBoolean(read["valid"]);
                    info.Role = Convert.ToInt32(read["roles"]);
                    info.Mark = "" + read["mark"];
                    return info;
                }
                return null;
            }
            catch (Exception)
            {
                throw;
            }
            finally
            {
                con.Close();
            }
        }

        public int Register(UserInfo info)
        {
            string sql = "insert into Users values (@name,@psd,@roles,@nick,@gender,@email,@valid,@date,@mark)";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter[] paras = {
                                       new SqlParameter("@name", info.UserName),
                                       new SqlParameter("@psd", info.UserPass),
                                       new SqlParameter("@roles", info.Role),
                                       new SqlParameter("@nick", info.NickName),
                                       new SqlParameter("@gender", info.Gender),
                                       new SqlParameter("@email", info.Email),
                                       new SqlParameter("@valid", true),
                                       new SqlParameter("@date", DateTime.Now),
                                       new SqlParameter("@mark", info.Mark)
                                   };
            cmd.Parameters.AddRange(paras);
            try
            {
                con.Open();
                return cmd.ExecuteNonQuery();
            }
            catch (Exception e)
            {
                Log.e(e.Message);
                return -1;
            }
            finally
            {
                con.Close();
            }
        }

        public object checkUserforName(string name)
        {
            string sql = "select COUNT(1) from Users where name=@name";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter para = new SqlParameter("@name", name);
            cmd.Parameters.Add(para);
            try
            {
                con.Open();
                return cmd.ExecuteScalar();
            }
            catch (Exception)
            {
                throw;
            }
            finally
            {
                con.Close();
            }
        }

        public object checkUserforEmail(string email)
        {
            string sql = "select COUNT(1) from Users where email=@email";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter para = new SqlParameter("@email", email);
            cmd.Parameters.Add(para);
            try
            {
                con.Open();
                return cmd.ExecuteScalar();
            }
            catch (Exception)
            {
                throw;
            }
            finally
            {
                con.Close();
            }
        }

        public int updateUser(UserInfo info)
        {
            string sql = "update Users set roles=@roles,nickname=@nickname,gender=@gender,mark=@mark where id=@id";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter[] paras = {
                                       new SqlParameter("@id", info.ID),
                                       new SqlParameter("@roles",info.Role),
                                       new SqlParameter("@nickname",info.NickName),
                                       new SqlParameter("@gender",info.Gender),
                                       new SqlParameter("@mark",info.Mark)
                                   };
            cmd.Parameters.AddRange(paras);
            try
            {
                con.Open();
                return cmd.ExecuteNonQuery();
            }
            catch (Exception e)
            {
                Log.e(e.Message);
                return -1;
            }
            finally
            {
                con.Close();
            }
        }

        public int updateFillUser(UserInfo info)
        {
            string sql = "update Users set name=@name,psd=@psd,roles=@roles,nickname=@nickname,gender=@gender,valid=@valid,mark=@mark where id=@id";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter[] paras = {
                                       new SqlParameter("@id", info.ID),
                                       new SqlParameter("@name", info.UserName),
                                       new SqlParameter("@psd", info.UserPass),
                                       new SqlParameter("@roles",info.Role),
                                       new SqlParameter("@nickname",info.NickName),
                                       new SqlParameter("@gender",info.Gender),
                                       new SqlParameter("@valid", info.Valid),
                                       new SqlParameter("@mark",info.Mark)
                                   };
            cmd.Parameters.AddRange(paras);
            try
            {
                con.Open();
                return cmd.ExecuteNonQuery();
            }
            catch (Exception e)
            {
                Log.e(e.Message);
                return -1;
            }
            finally
            {
                con.Close();
            }
        }

        public int changePassword(int id, string password)
        {
            string sql = "update Users set psd=@psd where id=@id";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter[] paras = {
                                       new SqlParameter("@id",id),
                                       new SqlParameter("@psd",password)
                                   };
            cmd.Parameters.AddRange(paras);
            try
            {
                con.Open();
                return cmd.ExecuteNonQuery();
            }
            catch (Exception e)
            {
                Log.e(e.Message);
                return -1;
            }
            finally
            {
                con.Close();
            }
        }

        public int deleteUser(int id)
        {
            string sql = "delete from Users where id=@id";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter para = new SqlParameter("@id", id);
            cmd.Parameters.Add(para);
            try
            {
                con.Open();
                return cmd.ExecuteNonQuery();
            }
            catch (Exception e)
            {
                Log.e(e.Message);
                return -1;
            }
            finally
            {
                con.Close();
            }
        }
        #endregion

        #region 注册码数据表操作
        public DataSet getAllKeys()
        {
            string sql = "select * from Keys order by id desc";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            try
            {
                con.Open();
                SqlDataAdapter sda = new SqlDataAdapter(cmd);
                DataSet ds = new DataSet();
                sda.Fill(ds);
                return ds;
            }
            catch (Exception)
            {
                throw;
            }
            finally
            {
                con.Close();
            }
        }

        public DataSet getKey(string key)
        {
            string sql = "select * from Keys where keys=@key";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter para = new SqlParameter("@key", key);
            cmd.Parameters.Add(para);
            try
            {
                con.Open();
                SqlDataAdapter sda = new SqlDataAdapter(cmd);
                DataSet ds = new DataSet();
                sda.Fill(ds);
                return ds;
            }
            catch (Exception)
            {
                throw;
            }
            finally
            {
                con.Close();
            }
        }

        public DataSet getKey()
        {
            string sql = "select keys,(maxc-curr)as timed from Keys where ver=@key order by timed desc";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter para = new SqlParameter("@key", 's');
            cmd.Parameters.Add(para);
            try
            {
                con.Open();
                SqlDataAdapter sda = new SqlDataAdapter(cmd);
                DataSet ds = new DataSet();
                sda.Fill(ds);
                return ds;
            }
            catch (Exception)
            {
                throw;
            }
            finally
            {
                con.Close();
            }
        }

        public object getKeyCount(string key)
        {
            string sql = "select Count(1) from Keys where keys=@key";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter para = new SqlParameter("@key", key);
            cmd.Parameters.Add(para);
            try
            {
                con.Open();
                return cmd.ExecuteScalar();
            }
            catch (Exception)
            {
                throw;
            }
            finally
            {
                con.Close();
            }
        }

        public int addKeys(string key, int max, string ver, string mark)
        {
            string sql = "insert into Keys (keys,maxc,curr,ver,cdate,mark) values (@key,@max,@cur,@ver,@date,@mark)";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter[] paras = {
                                       new SqlParameter("@key", key),
                                       new SqlParameter("@max", max),
                                       new SqlParameter("@cur", SqlDbType.Int),
                                       new SqlParameter("@ver", SqlDbType.Char, 1),
                                       new SqlParameter("@date", DateTime.Now),
                                       new SqlParameter("@mark", mark)
                                   };
            paras[2].Value = 0;
            paras[3].Value = ver;
            cmd.Parameters.AddRange(paras);
            try
            {
                con.Open();
                return cmd.ExecuteNonQuery();
            }
            catch (Exception e)
            {
                Log.e(e.Message);
                return -1;
            }
            finally
            {
                con.Close();
            }
        }

        public int registKey(string key)
        {
            string sql = "update Keys set curr=curr+1 where keys=@key and maxc-curr > 0";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter para = new SqlParameter("@key", key);
            cmd.Parameters.Add(para);
            try
            {
                con.Open();
                return cmd.ExecuteNonQuery();
            }
            catch (Exception e)
            {
                Log.e(e.Message);
                return -1;
            }
            finally
            {
                con.Close();
            }
        }

        public int deleteKey(int id)
        {
            string sql = "delete from Keys where id=@id";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter para = new SqlParameter("@id", id);
            cmd.Parameters.Add(para);
            try
            {
                con.Open();
                return cmd.ExecuteNonQuery();
            }
            catch (Exception e)
            {
                Log.e(e.Message);
                return -1;
            }
            finally
            {
                con.Close();
            }
        }
        #endregion

        #region 视频数据表操作
        public DataSet getAllVideo()
        {
            string sql = "select * from Video";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            try
            {
                con.Open();
                SqlDataAdapter sda = new SqlDataAdapter(cmd);
                DataSet ds = new DataSet();
                sda.Fill(ds);
                return ds;
            }
            catch (Exception)
            {
                throw;
            }
            finally
            {
                con.Close();
            }
        }

        public object getVideoUrl(string id)
        {
            string sql = "select url from Video where id=@id";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter para = new SqlParameter("@id", id);
            cmd.Parameters.Add(para);
            try
            {
                con.Open();
                return cmd.ExecuteScalar();
            }
            catch (Exception)
            {
                throw;
            }
            finally
            {
                con.Close();
            }
        }
        #endregion

        #region 语录表数据操作
        public DataSet getQuotations()
        {
            string sql = "select * from Quotations where DATEDIFF(dd,cdate,GetDate())=0 or tips=1";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            try
            {
                con.Open();
                SqlDataAdapter sda = new SqlDataAdapter(cmd);
                DataSet ds = new DataSet();
                sda.Fill(ds);
                return ds;
            }
            catch (Exception)
            {
                throw;
            }
            finally
            {
                con.Close();
            }
        }

        public DataSet getAllQuotations()
        {
            string sql = "select * from Quotations";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            try
            {
                con.Open();
                SqlDataAdapter sda = new SqlDataAdapter(cmd);
                DataSet ds = new DataSet();
                sda.Fill(ds);
                return ds;
            }
            catch (Exception)
            {
                throw;
            }
            finally
            {
                con.Close();
            }
        }

        public DataSet getQuotation(int id)
        {
            string sql = "select * from Quotations where id=@id";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter para = new SqlParameter("@id", id);
            cmd.Parameters.Add(para);
            try
            {
                con.Open();
                SqlDataAdapter sda = new SqlDataAdapter(cmd);
                DataSet ds = new DataSet();
                sda.Fill(ds);
                return ds;
            }
            catch (Exception)
            {
                throw;
            }
            finally
            {
                con.Close();
            }
        }

        public int addQuotations(string value, bool tips)
        {
            string sql = "insert into Quotations values (@sentence,@date,@tip)";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter[] paras = {
                                       new SqlParameter("@sentence", value),
                                       new SqlParameter("@date", DateTime.Now),
                                       new SqlParameter("@tip", tips)
                                   };
            cmd.Parameters.AddRange(paras);
            try
            {
                con.Open();
                return cmd.ExecuteNonQuery();
            }
            catch (Exception e)
            {
                Log.e(e.Message);
                return -1;
            }
            finally
            {
                con.Close();
            }
        }

        public int updateQuotations(int id, string value, bool tips, DateTime cdate)
        {
            string sql = "update Quotations set sentence=@sentence,cdate=@date,tips=@tip where id=@id";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter[] paras = {
                                       new SqlParameter("@sentence", value),
                                       new SqlParameter("@date", cdate),
                                       new SqlParameter("@tip", tips),
                                       new SqlParameter("@id", id)
                                   };
            cmd.Parameters.AddRange(paras);
            try
            {
                con.Open();
                return cmd.ExecuteNonQuery();
            }
            catch (Exception e)
            {
                Log.e(e.Message);
                return -1;
            }
            finally
            {
                con.Close();
            }
        }

        public int delQuotations(int id)
        {
            string sql = "delete from Quotations where id=@id";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter para = new SqlParameter("@id", id);
            cmd.Parameters.Add(para);
            try
            {
                con.Open();
                return cmd.ExecuteNonQuery();
            }
            catch (Exception e)
            {
                Log.e(e.Message);
                return -1;
            }
            finally
            {
                con.Close();
            }
        }
        #endregion

        #region 知识点表数据操作
        public DataSet getKnowledge()
        {
            string sql = "select * from Knowledge where valid=1 order by comment desc,edate desc";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            try
            {
                con.Open();
                SqlDataAdapter sda = new SqlDataAdapter(cmd);
                DataSet ds = new DataSet();
                sda.Fill(ds);
                return ds;
            }
            catch (Exception)
            {
                throw;
            }
            finally
            {
                con.Close();
            }
        }

        public DataSet getKnowledge(int count, int pageindex)
        {
            string sql = "select top(" + count + ")* from Knowledge where valid=1 and id not in(select top(" + count + "*(" + pageindex + "-1)) id from Knowledge where valid=1 order by comment desc,edate desc) order by comment desc,edate desc";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            try
            {
                con.Open();
                SqlDataAdapter sda = new SqlDataAdapter(cmd);
                DataSet ds = new DataSet();
                sda.Fill(ds);
                return ds;
            }
            catch (Exception)
            {
                throw;
            }
            finally
            {
                con.Close();
            }
        }

        public DataSet getKnowledge(int id)
        {
            string sql = "select * from Knowledge where id=@id";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter para = new SqlParameter("@id", id);
            cmd.Parameters.Add(para);
            try
            {
                con.Open();
                SqlDataAdapter sda = new SqlDataAdapter(cmd);
                DataSet ds = new DataSet();
                sda.Fill(ds);
                return ds;
            }
            catch (Exception)
            {
                throw;
            }
            finally
            {
                con.Close();
            }
        }

        public DataSet getRecycleKnowledge()
        {
            string sql = "select * from Knowledge where valid=0 order by edate desc";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            try
            {
                con.Open();
                SqlDataAdapter sda = new SqlDataAdapter(cmd);
                DataSet ds = new DataSet();
                sda.Fill(ds);
                return ds;
            }
            catch (Exception)
            {
                throw;
            }
            finally
            {
                con.Close();
            }
        }

        public int updateKnowledge(int id, string title, string artical, string user)
        {
            string sql = "update Knowledge set title=@title,artical=@artical,edate=@edate,euser=@euser where id=@id";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter[] paras = {
                                       new SqlParameter("@title", title),
                                       new SqlParameter("@artical", artical),
                                       new SqlParameter("@edate", DateTime.Now),
                                       new SqlParameter("@euser", user),
                                       new SqlParameter("@id", id)
                                   };
            cmd.Parameters.AddRange(paras);
            try
            {
                con.Open();
                return cmd.ExecuteNonQuery();
            }
            catch (Exception e)
            {
                Log.e(e.Message);
                return -1;
            }
            finally
            {
                con.Close();
            }
        }

        public int updateKnowledge(int id, bool valid)
        {
            string sql = "update Knowledge set valid=@valid where id=@id";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter[] paras = {
                                       new SqlParameter("@valid", valid),
                                       new SqlParameter("@id", id)
                                   };
            cmd.Parameters.AddRange(paras);
            try
            {
                con.Open();
                return cmd.ExecuteNonQuery();
            }
            catch (Exception e)
            {
                Log.e(e.Message);
                return -1;
            }
            finally
            {
                con.Close();
            }
        }

        public int addKnowledgeRead(int id)
        {
            string sql = "update Knowledge set cread=cread+1 where id=@id";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter para = new SqlParameter("@id", id);
            cmd.Parameters.Add(para);
            try
            {
                con.Open();
                return cmd.ExecuteNonQuery();
            }
            catch (Exception e)
            {
                Log.e(e.Message);
                return -1;
            }
            finally
            {
                con.Close();
            }
        }

        public int addKnowledgeComment(int id)
        {
            string sql = "update Knowledge set comment=@comment where id=@id";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter[] paras = {
                                       new SqlParameter("@id", id),
                                       new SqlParameter("@comment", DateTime.Now)
                                   };
            cmd.Parameters.AddRange(paras);
            try
            {
                con.Open();
                return cmd.ExecuteNonQuery();
            }
            catch (Exception e)
            {
                Log.e(e.Message);
                return -1;
            }
            finally
            {
                con.Close();
            }
        }

        public int addKnowledge(string title, string artical, string user)
        {
            string sql = "insert into Knowledge (title,artical,cdate,cuser,edate,comment,cread,valid) values (@title,@artical,@date,@cuser,@edate,@comment,@cread,@valid)";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            DateTime start = DateTime.Now;
            SqlParameter[] paras = {
                                       new SqlParameter("@title", title),
                                       new SqlParameter("@artical", artical),
                                       new SqlParameter("@date", start),
                                       new SqlParameter("@cuser", user),
                                       new SqlParameter("@comment", start),
                                       new SqlParameter("@edate", start),
                                       new SqlParameter("@cread", SqlDbType.Int),
                                       new SqlParameter("@valid", true)
                                   };
            paras[1].SqlDbType = SqlDbType.Text;
            paras[6].Value = 0;
            cmd.Parameters.AddRange(paras);
            try
            {
                con.Open();
                return cmd.ExecuteNonQuery();
            }
            catch (Exception e)
            {
                Log.e(e.Message);
                return -1;
            }
            finally
            {
                con.Close();
            }
        }

        public int deleteKnowledge(int id)
        {
            string sql = "delete from Knowledge where id=@id";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter para = new SqlParameter("@id", id);
            cmd.Parameters.Add(para);
            try
            {
                con.Open();
                return cmd.ExecuteNonQuery();
            }
            catch (Exception e)
            {
                Log.e(e.Message);
                return -1;
            }
            finally
            {
                con.Close();
            }
        }

        public int clearKnowledge()
        {
            string sql = "delete from Knowledge where valid=@valid";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter para = new SqlParameter("@valid", false);
            cmd.Parameters.Add(para);
            try
            {
                con.Open();
                return cmd.ExecuteNonQuery();
            }
            catch (Exception e)
            {
                Log.e(e.Message);
                return -1;
            }
            finally
            {
                con.Close();
            }
        }
        #endregion

        #region 评论表数据操作
        public int addComment(int aid, string uid, int star, string comment)
        {
            string sql = "insert into Comment (aid,uid,star,comment,cdate) values (@aid,@uid,@star,@comment,@cdate)";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter[] paras = {
                                       new SqlParameter("@aid", aid),
                                       new SqlParameter("@uid", uid),
                                       new SqlParameter("@star", star),
                                       new SqlParameter("@comment", comment),
                                       new SqlParameter("@cdate", DateTime.Now)
                                   };
            cmd.Parameters.AddRange(paras);
            try
            {
                con.Open();
                return cmd.ExecuteNonQuery();
            }
            catch (Exception e)
            {
                Log.e(e.Message);
                return -1;
            }
            finally
            {
                con.Close();
            }
        }

        public int addComment(int aid, string uid, int star, string comment, string device)
        {
            string sql = "insert into Comment values (@aid,@uid,@star,@comment,@cdate,@device)";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter[] paras = {
                                       new SqlParameter("@aid", aid),
                                       new SqlParameter("@uid", uid),
                                       new SqlParameter("@star", star),
                                       new SqlParameter("@comment", comment),
                                       new SqlParameter("@cdate", DateTime.Now),
                                       new SqlParameter("@device", device)
                                   };
            cmd.Parameters.AddRange(paras);
            try
            {
                con.Open();
                return cmd.ExecuteNonQuery();
            }
            catch (Exception e)
            {
                Log.e(e.Message);
                return -1;
            }
            finally
            {
                con.Close();
            }
        }

        public DataSet getComment(int aid)
        {
            string sql = "select * from Comment where aid=@aid order by cdate desc";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter para = new SqlParameter("@aid", aid);
            cmd.Parameters.Add(para);
            try
            {
                con.Open();
                SqlDataAdapter sda = new SqlDataAdapter(cmd);
                DataSet ds = new DataSet();
                sda.Fill(ds);
                return ds;
            }
            catch (Exception)
            {
                throw;
            }
            finally
            {
                con.Close();
            }
        }

        public int deleteComment(int id)
        {
            string sql = "delete from Comment where id=@id";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter para = new SqlParameter("@id", id);
            cmd.Parameters.Add(para);
            try
            {
                con.Open();
                return cmd.ExecuteNonQuery();
            }
            catch (Exception e)
            {
                Log.e(e.Message);
                return -1;
            }
            finally
            {
                con.Close();
            }
        }
        #endregion

        #region 反馈表数据操作
        public DataSet getFeedback()
        {
            string sql = "select * from Feedback order by id desc";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            try
            {
                con.Open();
                SqlDataAdapter sda = new SqlDataAdapter(cmd);
                DataSet ds = new DataSet();
                sda.Fill(ds);
                return ds;
            }
            catch (Exception)
            {
                throw;
            }
            finally
            {
                con.Close();
            }
        }

        public int addFeedback(string feed, string device, string contact)
        {
            string sql = "insert into Feedback values (@suggest,@device,@contact,@cdate)";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter[] paras = {
                                       new SqlParameter("@suggest", feed),
                                       new SqlParameter("@device", device),
                                       new SqlParameter("@contact", contact),
                                       new SqlParameter("@cdate", DateTime.Now)
                                   };
            cmd.Parameters.AddRange(paras);
            try
            {
                con.Open();
                return cmd.ExecuteNonQuery();
            }
            catch (Exception e)
            {
                Log.e(e.Message);
                return -1;
            }
            finally
            {
                con.Close();
            }
        }

        public int delFeedback(int id)
        {
            string sql = "delete from Feedback where id=@id";
            SqlConnection con = new SqlConnection(conn);
            SqlCommand cmd = new SqlCommand(sql, con);
            SqlParameter para = new SqlParameter("@id", id);
            cmd.Parameters.Add(para);
            try
            {
                con.Open();
                return cmd.ExecuteNonQuery();
            }
            catch (Exception e)
            {
                Log.e(e.Message);
                return -1;
            }
            finally
            {
                con.Close();
            }
        }
        #endregion
    }
}
