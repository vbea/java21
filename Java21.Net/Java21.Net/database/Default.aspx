<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="Default.aspx.cs" Inherits="Database_Tools.Default" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>Database Tools</title>
    <link href="../css/home.css" rel="stylesheet" />
</head>
<body style="background-color:White;">
    <form id="form1" runat="server">
    <div style="width: 100%; text-align: center;">
        <div style="text-align: center;">
            <font size="+2">Database Tools v3.0</font>
        </div>
        <br />
        <br />
        <br />
        <br />
        <br />
        <div style="width: 100%; padding: 10px; text-align: center;">
            <p>
                连接数据库<asp:ImageButton ID="imbGoto" runat="server" EnableTheming="False" ImageUrl="~/images/goto.jpg"
                    OnClick="imbGoto_Click" ToolTip="直接进入" />
            </p>
            <table style="width:100%; text-align:center;">
                <tr>
                    <td style="width:50%; text-align:right;">
                        数据库服务器：
                    </td>
                    <td style="width:50%; text-align:left;">
                        <asp:TextBox ID="txtServerName" runat="server" Width="178px"></asp:TextBox>
                        （<asp:LinkButton ID="likTest" runat="server" OnClick="btnTest_Click">测试</asp:LinkButton>）
                    </td>
                </tr>
                <tr>
                    <td colspan="2" style="width:50%; text-align:center;">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <asp:RadioButton ID="rdbWin" runat="server" GroupName="val" OnCheckedChanged="rdbValidate_CheckedChanged"
                            Text="Windows验证" AutoPostBack="True" />
                        <asp:RadioButton ID="rdbSql" runat="server" GroupName="val" OnCheckedChanged="rdbValidate_CheckedChanged"
                            Text="Sql Server验证" AutoPostBack="True" Checked="True" />
                    </td>
                </tr>
                <asp:Panel ID="palLogin" runat="server">
                    <tr>
                        <td style="width:50%; text-align:right;">
                            用户名：
                        </td>
                        <td style="width:50%; text-align:left;">
                            <asp:TextBox ID="txtUserName" runat="server" Width="178px"></asp:TextBox>
                        </td>
                     </tr>
                     <tr>
                        <td style="width:50%; text-align:right;">
                            密&nbsp; &nbsp; 码：
                        </td>
                        <td style="width:50%; text-align:left;">
                            <asp:TextBox ID="txtPsd" runat="server" Width="178px"></asp:TextBox>
                        </td>
                    </tr>
                </asp:Panel>
                <tr>
                    <td colspan="2">
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <asp:Button ID="btnConnect" runat="server" Text=" 连 接 " Width="62px" OnClick="btnConnect_Click" />
                    </td>
                </tr>
            </table>
        </div>
    </div>
    </form>
</body>
</html>
