using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Java21.Model
{
    public class UserInfo
    {
        public int ID { get; set; }
        /// <summary>
        /// 用户名
        /// </summary>
        public string UserName { get; set; }
        /// <summary>
        /// 密码
        /// </summary>
        public string UserPass { get; set; }
        /// <summary>
        /// 性别（0:男;1:女）
        /// </summary>
        public int Gender { get; set; }
        /// <summary>
        /// 用户类型
        /// </summary>
        public int Role { get; set; }
        /// <summary>
        /// 昵称
        /// </summary>
        public string NickName { get; set; }
        /// <summary>
        /// 邮箱
        /// </summary>
        public string Email { get; set; }
        /// <summary>
        /// 说明
        /// </summary>
        public string Mark { get; set; }
        /// <summary>
        /// 是否有效
        /// </summary>
        public bool Valid { get; set; }

        /// <summary>
        /// 管理员
        /// </summary>
        public const int ROLE_ADMIN = 1;
        /// <summary>
        /// 普通用户
        /// </summary>
        public const int ROLE_USER = 2;
        /// <summary>
        /// 游客（未启用）
        /// </summary>
        public const int ROLE_GUEST = 3;
        /// <summary>
        /// 受限用户
        /// </summary>
        public const int ROLE_CONF = 4;
    }
}
