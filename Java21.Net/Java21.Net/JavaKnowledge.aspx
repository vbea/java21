<%@ Page Language="C#" AutoEventWireup="true" CodeBehind="JavaKnowledge.aspx.cs" Inherits="Java21.Net.JavaKnowledge" %>

<!DOCTYPE html>

<html xmlns="http://www.w3.org/1999/xhtml">
<head runat="server">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
    <meta name="keywords" content="21天学通Java,邠心,邠心工作室,Android软件开发,Java学习,BXA学堂,正德应用,自学Java" />
    <meta name="description" content="《21天学通Java》-邠心工作室-Java学习网站-在线学习，Android软件开发" />
    <meta name="author" content="邠心" />
    <title>
        <asp:Literal ID="litTitle" runat="server" Text="Java知识点"></asp:Literal></title>
    <link rel="shortcut icon" href="images/favicon.ico" type="image/x-icon" />
    <link href="css/bootstrap.css" rel="stylesheet" />
    <link href="css/java.css" rel="stylesheet" />
    <script src="script/comment.js"></script>
    <script src='script/jquery.min.js'></script>
    <script src="script/jquery.qqFace.js"></script>
    <script src='script/mathquill/mathquill.js'></script>
    <link rel='stylesheet' href='script/mathquill/mathquill.css' />
    <link href="css/shCoreDefault.css" rel="stylesheet" />
    <script src="script/shCore.js"></script>
    <script src="script/shBrushJava.js"></script>
    <script type="text/javascript">
        SyntaxHighlighter.all();
        imgClear();
        $(function () {
            $('.emotion').qqFace({
                assign: 'txtComment',
                path: 'images/arclist/'
            });
        });
    </script>
</head>
<body>
    <form id="form1" runat="server">
        <asp:ScriptManager ID="scriptajax" runat="server"></asp:ScriptManager>
        <div style="width: 320px; padding: 0px; margin: 0px auto; text-align: center;">
            <asp:Label ID="labTitle" runat="server" Text="Java知识点" Style="width: 100%; font-size: 17px; font-weight: bold; margin:10px auto; display:block;"></asp:Label>
            <div style="text-align:left; color:gray; padding:5px;">
                阅(<asp:Literal ID="litStatistics" runat="server" Text="0"></asp:Literal>)
                评(<asp:Literal ID="litStatisticz" runat="server" Text="0"></asp:Literal>)
            </div>
            <div id="artical" runat="server" class="content">
            </div>
            <div id="writer" runat="server" style="width: 98%; text-align: right; padding-right: 5px; color: gray; font-size: 12px;"></div>
            <div style="text-align: left; padding: 1px 5px; font-size: 14px;">
                <asp:UpdatePanel ID="ajaxpan" runat="server">
                    <ContentTemplate>
                        <table style="width: 100%;">
                            <tr>
                                <td colspan="2" style="width: 100%; padding-left: 5px;">评分：
                                    <img id="imgStar1" src="images/ic_star_normal.png" onmouseover="MouseOver(1)" onmouseout="MouseOut()" onclick="imgClick(1)" alt="极差" />
                                    <img id="imgStar2" src="images/ic_star_normal.png" onmouseover="MouseOver(2)" onmouseout="MouseOut()" onclick="imgClick(2)" alt="不好" />
                                    <img id="imgStar3" src="images/ic_star_normal.png" onmouseover="MouseOver(3)" onmouseout="MouseOut()" onclick="imgClick(3)" alt="一般" />
                                    <img id="imgStar4" src="images/ic_star_normal.png" onmouseover="MouseOver(4)" onmouseout="MouseOut()" onclick="imgClick(4)" alt="较好" />
                                    <img id="imgStar5" src="images/ic_star_normal.png" onmouseover="MouseOver(5)" onmouseout="MouseOut()" onclick="imgClick(5)" alt="极好" />
                                    <asp:HiddenField ID="hidStart" runat="server" ClientIDMode="Static" Value="0" />
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" style="width: 100%; padding-right: 8px;">
                                    <asp:TextBox ID="txtComment" runat="server" Height="50px" TextMode="MultiLine" Width="98%" Style="margin: 3px auto;" ClientIDMode="Static"></asp:TextBox>
                                </td>
                            </tr>
                            <tr>
                                <td style="text-align: left;">
                                    <asp:Image ID="imgPhone" runat="server" ImageUrl="~/images/phone.png" Visible="False" />
                                    <asp:Label ID="labError" runat="server" ForeColor="Red" ClientIDMode="Static" Font-Size="Small"></asp:Label>
                                </td>
                                <td style="text-align: right;">
                                    <span class="emotion">
                                        <img src="images/face.png" style="padding-right:10px;"/></span>
                                    <asp:Button ID="btnComment" runat="server" Text="发表评论" CssClass="btn btn-info" OnClick="btnComment_Click" OnClientClick="return check();" Enabled="False" /></td>
                            </tr>
                        </table>
                        <div style="width: 100%; height: 10px; background-color: #EEEEEE; margin: 2px auto;"></div>
                        <asp:GridView ID="gvComment" runat="server" Width="100%" AllowPaging="True" AutoGenerateColumns="False" ShowHeader="False" OnPageIndexChanging="gvComment_PageIndexChanging" GridLines="None">
                            <Columns>
                                <asp:TemplateField>
                                    <ItemTemplate>
                                        <table style="width: 100%; text-align: left; color: #808080;">
                                            <tr>
                                                <td style="width: 40%; padding: 1px 5px; vertical-align:top;">
                                                    <%# Eval("uid") + "：" + getStar(Eval("star"),Eval("Device")) %>
                                                </td>
                                                <td style="width: 40%; padding: 1px 5px; text-align: right; vertical-align:top;">
                                                    <%# Convert.ToDateTime(Eval("cdate")).ToString("MM-dd HH:mm") %>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td colspan="2" style="width: 98%; padding: 5px;"><%# show_content(Eval("comment").ToString()) %></td>
                                            </tr>
                                        </table>
                                    </ItemTemplate>
                                </asp:TemplateField>
                            </Columns>
                            <EmptyDataTemplate>
                                <div style="width: 100%; text-align: center; color: #CCCCCC;">还没有人评论，快来抢沙发吧！</div>
                            </EmptyDataTemplate>
                            <PagerStyle CssClass="pager_in" />
                            <RowStyle BackColor="white" CssClass="com_row" />
                        </asp:GridView>
                    </ContentTemplate>
                </asp:UpdatePanel>
            </div>
        </div>
    </form>
</body>
</html>
