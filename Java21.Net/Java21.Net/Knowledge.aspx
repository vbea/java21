<%@ Page Title="" Language="C#" MasterPageFile="~/Java.Master" AutoEventWireup="true" CodeBehind="Knowledge.aspx.cs" Inherits="Java21.Net.Knowledge" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <title>Java知识点</title>
    <link href="script/asyncbox/skins/ZCMS/asyncbox.css" rel="stylesheet" />
    <script type="text/javascript" src="script/cooke.js"></script>
    <script type="text/javascript" src="script/asyncbox/AsyncBox.v1.4.5.js"></script>
    <script type="text/javascript">
        function showAdd() {
            asyncbox.open({
                id: "add_artical",
                url: 'AddKnowledges.aspx',
                width: 400,
                height: 400,
                data: { "action": "add" },
                title: '添加内容',
            });
        }
        function showEdit(id) {
            asyncbox.open({
                id: "add_artical",
                url: 'AddKnowledges.aspx',
                width: 400,
                height: 450,
                data: { "action": "edit", "id": id },
                title: '修改内容',
            });
        }
        function closeDialog() {
            asyncbox.close("add_artical");
            __doPostBack('ctl00$ContentPlaceHolder1$hidAjax', '');
        }
    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <asp:UpdatePanel ID="ajax" runat="server">
        <ContentTemplate>
            <asp:LinkButton ID="hidAjax" runat="server" OnClick="hidAjax_Click"></asp:LinkButton>
            <asp:GridView ID="gvKey" runat="server" AutoGenerateColumns="False" Width="90%" AllowPaging="True" OnPageIndexChanging="gvKey_PageIndexChanging" ShowHeaderWhenEmpty="True" Style="margin: 10px auto;" PageSize="20" GridLines="None" OnRowDataBound="gvKey_RowDataBound">
                <Columns>
                    <asp:TemplateField>
                        <HeaderTemplate>
                            <table style="width: 100%; text-align: center;">
                                <tr>
                                    <td style="width: 10%; text-align: left; padding-left: 5px;">
                                        <asp:LinkButton ID="linRecycle" runat="server" Visible-='<%# Visi %>' ForeColor="White" OnClick="linRecycle_Click">回收站</asp:LinkButton></td>
                                    <td style="width: 80%; text-align: center;">Java知识点</td>
                                    <td style="width: 10%; text-align: right; padding-right: 5px;"><a href="Knowledges.aspx?action=add" style="color:white;">发表</a>
                                    </td>
                                </tr>
                            </table>
                        </HeaderTemplate>
                        <ItemTemplate>
                            <a href='<%# "KnowledgeView.aspx?id=" + Eval("id") %>' class="jar_a">
                                <table style="width: 100%; text-align: left;">
                                    <tr>
                                        <td style="width: 50%; text-align: left; padding: 2px 5px;"><%# Eval("title") %></td>
                                        <td style="width: 30%; text-align: right; padding-right: 25px; color: gray;">
                                            <%# getFoot(Eval("cdate"), Eval("cuser"), Eval("edate"), Eval("euser")) %>
                                        </td>
                                        <td style="width: 5%; text-align: left; padding-right: 5px; color: gray;">
                                            <%# Eval("cread")%>
                                        </td>
                                    </tr>
                                </table>
                            </a>
                        </ItemTemplate>
                    </asp:TemplateField>
                </Columns>
                <EmptyDataTemplate>
                    <div style="width: 100%; text-align: center;">当前没有数据，请添加！</div>
                </EmptyDataTemplate>
                <FooterStyle BackColor="#4780AE" Font-Bold="True" ForeColor="White" />
                <HeaderStyle BackColor="#4780AE" Font-Bold="True" ForeColor="White" CssClass="gv_hrader" />
                <PagerSettings FirstPageText="首页" LastPageText="尾页" Mode="NextPreviousFirstLast" NextPageText="下一页" PreviousPageText="上一页" />
                <PagerStyle CssClass="pager_in" />
                <RowStyle BackColor="white" CssClass="gv2_row" />
            </asp:GridView>
        </ContentTemplate>
    </asp:UpdatePanel>
</asp:Content>
