<%@ Page Title="" Language="C#" MasterPageFile="~/Java.Master" AutoEventWireup="true" CodeBehind="Quotations.aspx.cs" Inherits="Java21.Net.Quotations" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <title>每日一言管理</title>
    <link href="/Script/asyncbox/skins/ZCMS/asyncbox.css" rel="stylesheet" />
    <script type="text/javascript" src="/Script/cooke.js"></script>
    <script type="text/javascript" src="/Script/asyncbox/AsyncBox.v1.4.5.js"></script>
    <script type="text/javascript">
        function showDialog() {
            asyncbox.open({
                id: "edit_quota",
                url: 'AddQuota.aspx',
                width: 300,
                height: 300,
                data: { "action": "add" },
                title: '添加名言/公告',
            });
        }

        function showDialogEdit(id) {
            asyncbox.open({
                id: "edit_quota",
                url: 'AddQuota.aspx',
                width: 300,
                height: 300,
                data: { "action" : "edt", "id" : id },
                title: '修改名言/公告',
            });
        }

        function closeDialog() {
            asyncbox.close("edit_quota");
            __doPostBack('ctl00$ContentPlaceHolder1$linkData', '');
        }
    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <p>每日一言/公告管理</p>
    <asp:UpdatePanel ID="ajax" runat="server">
        <ContentTemplate>
            <asp:GridView ID="gvKey" runat="server" AutoGenerateColumns="False" Width="100%" AllowPaging="True" OnPageIndexChanging="gvKey_PageIndexChanging" CssClass="grid_b" GridLines="None" ShowFooter="True">
                <AlternatingRowStyle BackColor="#E7F7FF" />
                <Columns>
                    <asp:TemplateField>
                        <HeaderTemplate>
                            <table style="width: 100%; text-align: center;">
                                <tr>
                                    <td style="width: 8%;">ID</td>
                                    <td style="width: 65%;">内容</td>
                                    <td style="width: 10%;">创建时间</td>
                                    <td style="width: 5%;">Tip</td>
                                    <td style="width: 10%;">操作</td>
                                </tr>
                            </table>
                        </HeaderTemplate>
                        <ItemTemplate>
                            <table style="width: 100%; text-align: center;">
                                <tr>
                                    <td style="width: 8%;"><%# Eval("id") %></td>
                                    <td style="width: 65%; text-align: left;"><%# Eval("sentence") %></td>
                                    <td style="width: 10%;"><%# Convert.ToDateTime(Eval("cdate")).ToString("yyyy-MM-dd") %></td>
                                    <td style="width: 5%;"><%# Eval("tips") %></td>
                                    <td style="width: 10%;">
                                        <asp:LinkButton ID="lnkEdited" runat="server" Text="修改" OnClientClick='<%# "showDialogEdit(" + Eval("id") + ")" %>'></asp:LinkButton>
                                        <asp:LinkButton ID="lnkDelete" runat="server" Text="删除" CommandName='<%# Eval("id") %>' OnClientClick="return confirm('确定要删除吗？删除后不可恢复');" OnClick="lnkDelete_Click"></asp:LinkButton>
                                    </td>
                                </tr>
                            </table>
                        </ItemTemplate>
                    </asp:TemplateField>
                </Columns>
                <EmptyDataTemplate>
                    <div style="width: 100%; text-align: center;">当前没有数据，请添加！</div>
                </EmptyDataTemplate>
                <FooterStyle CssClass="pager_in" />
                <HeaderStyle BackColor="#4780AE" Font-Bold="True" ForeColor="White" CssClass="gv_hrader" />
                <PagerStyle CssClass="pager_in" />
                <RowStyle BackColor="white" CssClass="gv_row" />
            </asp:GridView>
            <asp:LinkButton ID="linkAdd" runat="server" OnClientClick="showDialog()">添加</asp:LinkButton>
        </ContentTemplate>
    </asp:UpdatePanel>
    <asp:LinkButton ID="linkData" runat="server" OnClick="linkAdd_Click"></asp:LinkButton>
</asp:Content>
