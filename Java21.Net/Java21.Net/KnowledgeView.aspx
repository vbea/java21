<%@ Page Title="" Language="C#" MasterPageFile="~/Java.Master" AutoEventWireup="true" CodeBehind="KnowledgeView.aspx.cs" Inherits="Java21.Net.KnowledgeView" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <title>
        <asp:Literal ID="litTitle" runat="server"></asp:Literal></title>
    <script src='script/jquery.min.js'></script>
    <script src='script/mathquill/mathquill.js'></script>
    <link rel='stylesheet' href='script/mathquill/mathquill.css' />
    <link href="css/shCoreDefault.css" rel="stylesheet" />
    <script src="script/comment.js"></script>
    <script src="script/jquery.qqFace.js"></script>
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
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div style="width: 100%; padding-top: 10px; margin: 0px auto;">
        <a href="Knowledge.aspx">Java知识点</a> - 在线学习
        <br />
        <div style="width: 100%; padding: 5px; margin: 0px auto; text-align: center;">
            <asp:Label ID="labTitle" runat="server" Text="Java知识点" Style="width: 100%; font-size: 17px; font-weight: bold;"></asp:Label>
            <table style="width:99%; margin: 0px auto;">
                <tr>
                    <td style="width:50%; text-align:left; color:gray;">
                        阅(<asp:Literal ID="litStatistics" runat="server" Text="0"></asp:Literal>)
                        评(<asp:Literal ID="litStatisticz" runat="server" Text="0"></asp:Literal>)
                    </td>
                    <td style="width:50%; text-align:right;">
                        <asp:LinkButton ID="linkEdit" runat="server" OnClick="linkEdit_Click" Visible="False">编辑</asp:LinkButton>
                        &nbsp;<asp:LinkButton ID="linkDelete" runat="server" Visible="False"  OnClientClick="return confirm('确定要删除吗？');" OnClick="linkDelete_Click">删除</asp:LinkButton></td>
                </tr>
            </table>
        </div>
        <div id="artical" runat="server" class="content">
        </div>
        <div style="text-align: left; padding: 1px 5px; font-size: 14px; padding-right: 5px;">
            <table style="width: 100%;">
                <tr>
                    <td style="width: 50%; padding-left: 5px;">
                        <div id="writer" runat="server" style="text-align: left; padding-right: 5px; color: gray; font-size: 12px;"></div>
                    </td>
                    <td id="share" runat="server" style="width: 50%;">
                        <div class="bdsharebuttonbox" style="float: right;"><a href="#" class="bds_more" data-cmd="more"></a><a href="#" class="bds_qzone" data-cmd="qzone" title="分享到QQ空间"></a><a href="#" class="bds_tsina" data-cmd="tsina" title="分享到新浪微博"></a><a href="#" class="bds_tqq" data-cmd="tqq" title="分享到腾讯微博"></a><a href="#" class="bds_weixin" data-cmd="weixin" title="分享到微信"></a><a href="#" class="bds_tieba" data-cmd="tieba" title="分享到百度贴吧"></a></div>
                        <script>window._bd_share_config = { "common": { "bdSnsKey": {}, "bdText": "", "bdMini": "2", "bdMiniList": false, "bdPic": "", "bdStyle": "0", "bdSize": "16" }, "share": {} }; with (document) 0[(getElementsByTagName('head')[0] || body).appendChild(createElement('script')).src = 'http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion=' + ~(-new Date() / 36e5)];</script>
                    </td>
                </tr>
            </table>
        </div>
        <div style="text-align: left; padding: 1px 5px; font-size: 14px; padding-right: 5px;">
            <asp:UpdatePanel ID="ajaxpan" runat="server">
                <ContentTemplate>
                    <table style="width: 100%;">
                        <tr>
                            <td style="width: 60%; padding-left: 5px;">评分：
                                <img id="imgStar1" src="images/ic_star_normal.png" onmouseover="MouseOver(1)" onmouseout="MouseOut()" onclick="imgClick(1)" alt="极差" />
                                <img id="imgStar2" src="images/ic_star_normal.png" onmouseover="MouseOver(2)" onmouseout="MouseOut()" onclick="imgClick(2)" alt="不好" />
                                <img id="imgStar3" src="images/ic_star_normal.png" onmouseover="MouseOver(3)" onmouseout="MouseOut()" onclick="imgClick(3)" alt="一般" />
                                <img id="imgStar4" src="images/ic_star_normal.png" onmouseover="MouseOver(4)" onmouseout="MouseOut()" onclick="imgClick(4)" alt="较好" />
                                <img id="imgStar5" src="images/ic_star_normal.png" onmouseover="MouseOver(5)" onmouseout="MouseOut()" onclick="imgClick(5)" alt="极好" />
                                <asp:HiddenField ID="hidStart" runat="server" ClientIDMode="Static" Value="0" />
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" style="width: 100%;">
                                <asp:TextBox ID="txtComment" runat="server" Height="50px" TextMode="MultiLine" Width="98%" Style="margin: 3px auto;" ClientIDMode="Static"></asp:TextBox>
                            </td>
                        </tr>
                        <tr>
                            <td style="width: 50%; text-align: left;">
                                <asp:Label ID="labError" runat="server" ForeColor="Red" ClientIDMode="Static"></asp:Label>
                            </td>
                            <td style="width: 30%; text-align: right; padding-right: 5px;">
                                <span class="emotion">
                                    <img src="images/face.png" /></span>
                                <asp:Button ID="btnComment" runat="server" Text="发表评论" CssClass="btn btn-info" OnClick="btnComment_Click" OnClientClick="return check();" Enabled="False" /></td>
                        </tr>
                    </table>
                    <div style="width: 100%; height: 10px; background-color: #EEEEEE; margin: 2px auto;"></div>
                    <asp:GridView ID="gvComment" runat="server" style="width:100%;" AllowPaging="True" AutoGenerateColumns="False" ShowHeader="False" OnPageIndexChanging="gvComment_PageIndexChanging" GridLines="None">
                        <Columns>
                            <asp:TemplateField>
                                <ItemTemplate>
                                    <table style="width: 100%; text-align: left; color: #808080;">
                                        <tr>
                                            <td style="width: 40%; padding: 1px 5px;">
                                                <%# Eval("uid") + "：" + getStar(Eval("star"),Eval("Device")) %>
                                            </td>
                                            <td style="width: 40%; padding: 1px 5px; text-align: right;">
                                                <%# Convert.ToDateTime(Eval("cdate")).ToString("MM-dd HH:mm") %>
                                                <asp:ImageButton ID="lmlDelete" runat="server" CommandName='<%# Eval("id") %>' Visible='<%# getDelComment(Eval("uid")) %>' ImageUrl="~/images/close.png" OnClientClick="return confirm('确定要删除该条评论？');" OnClick="lmlDelete_Click"></asp:ImageButton>
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
                            <div style="width: 100%; height:200px; text-align: center; color: #CCCCCC;">还没有人评论，快来抢沙发吧！</div>
                        </EmptyDataTemplate>
                        <PagerStyle CssClass="pager_in" />
                        <RowStyle BackColor="white" CssClass="com_row" />
                    </asp:GridView>
                </ContentTemplate>
            </asp:UpdatePanel>
        </div>
    </div>
</asp:Content>
