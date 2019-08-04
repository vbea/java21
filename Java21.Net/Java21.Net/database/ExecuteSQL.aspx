<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="ExecuteSQL.aspx.cs" Inherits="Database_Tools.ExecuteSQL" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <title>SQL执行</title>
    <link href="../css/home.css" rel="stylesheet" />
    <style type="text/css">
        .container .keyword
        {
            color: #0011FF;
        }
        .style1
        {
            color: black !important;
        }
        .style2
        {
            color: gray !important;
        }
        .err
        {
            color: Red;
        }
        .null
        {
            color: Gray;
        }
        .blue
        {
            color: Blue;
        }
    </style>
</head>
<body>
    <form id="form1" runat="server">
    <div class="top_div">
        <font size="+2">Database Tools v3.0</font>
        <br />
        <br />
        <br />
        <div style="width: 100%; height: 5%; padding-left: 1px; text-align: left; float: left;">
            <div style="float: left; padding-left: 5%">
                &nbsp;&nbsp; 数据库：<asp:DropDownList ID="ddlDatabase" runat="server" AutoPostBack="True"
                    OnSelectedIndexChanged="ddlDatabase_SelectedIndexChanged">
                    <asp:ListItem>请选择...</asp:ListItem>
                </asp:DropDownList>
                （<asp:CheckBox ID="chkSystemDatabase" runat="server" AutoPostBack="True" Checked="True"
                    OnCheckedChanged="chkSystemDatabase_CheckedChanged" Text="显示系统数据库" />）
                <asp:LinkButton ID="btnRefresh" runat="server" OnClick="btnRefresh_Click">刷新</asp:LinkButton>
            </div>
        </div>
        <div style="width: 100%; padding-left: 1px; text-align: center; float: left;">
            <hr style="width: 90%" />
        </div>
        <div style="width: 100%; min-height: 500px; padding: 0px; margin: 0px; background-color: #FEFEFE;
            overflow-y: auto;">
            <div style="width: 80%; height: 80%; position: absolute; top: 18%; bottom: 0px; padding: 0 5%;
                margin: 0 auto; left: 0px; right: 0px; z-index: 100">
                <table style="width: 100%; height: 100%; border: 1px solid #aaa;">
                    <tr>
                        <td valign="top" style="width: 28%; padding: 5px 10px; text-align: left;">
                            你可以在右边的输入框中写下你要执行的SQL语句，多条语句分别执行请用go分隔(不区分大小写)<br />
                            <br />
                            例如：<br />
                            <div class="container">
                                <div>
                                    <code class="keyword">create</code><code class="keyword"> database</code><code class="style1">
                                        MyDatabase</code></div>
                                <div>
                                    <code class="keyword">go</code></div>
                                <div>
                                    <code class="keyword">use</code><code class="style1"> MyDatabase</code></div>
                                <div>
                                    <code class="keyword">go</code></div>
                                <div>
                                    <code class="keyword">create</code> <code class="keyword">table</code> <code class="style1">
                                        MyTable</code></div>
                                <div>
                                    <code class="style1">(</code></div>
                                <div>
                                    <code>&nbsp;&nbsp;&nbsp;&nbsp;</code><code class="style1">id </code><code class="keyword">
                                        int</code> <code class="keyword">primary</code> <code class="keyword">key</code>
                                    <code class="keyword">identity</code><code class="style1">(1,1),</code></div>
                                <div>
                                    <code>&nbsp;&nbsp;&nbsp;&nbsp;</code><code class="style1">name</code> <code class="keyword">
                                        varchar</code><code class="style1">(10)</code><code class="style2"> not</code><code
                                            class="style2"> null</code><code class="style1">,</code></div>
                                <div>
                                    <code>&nbsp;&nbsp;&nbsp;&nbsp;</code><code class="style1">marks </code><code class="keyword">
                                        varchar</code><code class="style1">(100)</code></div>
                                <div>
                                    <code class="style1">)</code></div>
                            </div>
                        </td>
                        <td valign="top" rowspan="2" style="width: 35%; height: 100%; padding: 5px 10px;">
                            <asp:TextBox ID="txtMuSql" runat="server" TextMode="MultiLine" Style="width: 100%;
                                height: 100%; text-align: left; border: none; padding: 5px;"></asp:TextBox>
                        </td>
                        <td valign="top" rowspan="2" style="width: 40%; height: 100%; padding: 5px 10px;">
                            <div id="divData" runat="server" visible="false" style="width: 400px; height: 500px; text-align: left;">
                                查询结果：<asp:DropDownList ID="ddlDataTable" runat="server" AutoPostBack="True" OnSelectedIndexChanged="ddlDataTable_SelectedIndexChanged"
                                    Width="200px">
                                </asp:DropDownList>
                                <asp:Panel ID="Panel1" runat="server" ScrollBars="auto" style="width:100%; height:100%; margin-top:10px;">
                                    <asp:GridView ID="gdDataTable" runat="server" Style="max-height: 100%; min-width:100%;">
                                        <AlternatingRowStyle BackColor="White" ForeColor="#284775" />
                                        <EditRowStyle BackColor="#999999" />
                                        <EmptyDataRowStyle Font-Italic="False" />
                                        <EmptyDataTemplate>
                                            <div style="text-align: center; width: 100%">
                                                没有查询到数据</div>
                                        </EmptyDataTemplate>
                                        <FooterStyle BackColor="#5D7B9D" Font-Bold="True" ForeColor="White" />
                                        <HeaderStyle BackColor="#1299FF" Font-Bold="True" ForeColor="White" />
                                        <PagerStyle BackColor="#284775" ForeColor="White" HorizontalAlign="Center" />
                                        <RowStyle BackColor="#F7F6F3" ForeColor="#333333" />
                                        <SelectedRowStyle BackColor="#E2DED6" Font-Bold="True" ForeColor="#333333" />
                                        <SortedAscendingCellStyle BackColor="#E9E7E2" />
                                        <SortedAscendingHeaderStyle BackColor="#506C8C" />
                                        <SortedDescendingCellStyle BackColor="#FFFDF8" />
                                        <SortedDescendingHeaderStyle BackColor="#6F8DAE" />
                                    </asp:GridView>
                                </asp:Panel>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td valign="middle" style="width: 28%; padding: 5px 10px; text-align: left;">
                            执行结果：<br />
                            <div id="txtResoult" runat="server" style="width: 100%; height: 50px; text-align: left;
                                padding: 5px;"></div>
                        </td>
                    </tr>
                    <tr>
                        <td>
                        </td>
                        <td colspan="1">
                            <asp:Button ID="btnBack" runat="server" Text="返回" OnClick="btnBack_Click" Style="width: 20%;
                                height: 30px; text-align: center; border: none; background-color: #4780AE; margin: 10px auto;
                                color: White;" />
                            &nbsp;&nbsp;
                            <asp:Button ID="btnExec" runat="server" Text="执行" OnClick="btnExec_Click" Style="width: 30%;
                                height: 30px; text-align: center; border: none; background-color: #4780AE; margin: 10px auto;
                                color: White;" />
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
    </form>
</body>
</html>
