<%@ Page Title="" Language="C#" MasterPageFile="~/Java.Master" AutoEventWireup="true" CodeBehind="UserMng.aspx.cs" Inherits="Java21.Net.UserMng" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <title>用户管理</title>
    <link href="/Script/asyncbox/skins/ZCMS/asyncbox.css" rel="stylesheet" />
    <script type="text/javascript" src="/Script/cooke.js"></script>
    <script type="text/javascript" src="/Script/asyncbox/AsyncBox.v1.4.5.js"></script>
    <script type="text/javascript">
        function showEditUser(id) {
            asyncbox.open({
                id: "edit_user",
                url: 'EditUser.aspx',
                width: 400,
                height: 400,
                data: { "id": id },
                title: '修改用户',
            });
        }
        function closeEditDialog() {
            asyncbox.close("edit_user");
            __doPostBack('ctl00$ContentPlaceHolder1$hidAjax','');
        }
    </script>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div style="width: 100%; height: 100%; padding: 0px; margin: 0 auto;">
        <p>用户列表</p><asp:LinkButton ID="hidAjax" runat="server" OnClick="hidAjax_Click" Text=""/>
        <asp:UpdatePanel ID="updddd" runat="server">
            <ContentTemplate>
                <asp:GridView ID="gvUser" runat="server" AutoGenerateColumns="False" Width="100%" AllowPaging="True" OnPageIndexChanging="gvUser_PageIndexChanging" CssClass="grid_b" GridLines="None">
                    <AlternatingRowStyle BackColor="#E7F7FF" />
                    <Columns>
                        <asp:TemplateField>
                            <HeaderTemplate>
                                <table style="width: 100%; text-align: center;">
                                    <tr>
                                        <td style="width: 5%;">ID</td>
                                        <td style="width: 10%;">用户名</td>
                                        <td style="width: 10%;">密码</td>
                                        <td style="width: 10%;">角色</td>
                                        <td style="width: 10%;">昵称</td>
                                        <td style="width: 5%;">性别</td>
                                        <td style="width: 15%;">邮箱</td>
                                        <td style="width: 5%;">活动</td>
                                        <td style="width: 15%;">注册日期</td>
                                        <td style="width: 10%;">操作</td>
                                    </tr>
                                </table>
                            </HeaderTemplate>
                            <ItemTemplate>
                                <table style="width: 100%; text-align: center;">
                                    <tr>
                                        <td style="width: 5%;"><%# Eval("id") %></td>
                                        <td style="width: 10%;"><%# Eval("name") %></td>
                                        <td style="width: 10%;"><span title='<%# Eval("psd") %>'>(已加密)</span></td>
                                        <td style="width: 10%;"><%# getRoles(Convert.ToInt32(Eval("roles"))) %></td>
                                        <td style="width: 10%;"><%# Eval("nickname") %></td>
                                        <td style="width: 5%;"><%# getGender(Eval("gender")) %></td>
                                        <td style="width: 15%;"><%# Eval("email") %></td>
                                        <td style="width: 5%;"><%# Eval("valid") %></td>
                                        <td style="width: 15%;"><%# Convert.ToDateTime(Eval("cdate")).ToString("yyyy-MM-dd HH:mm:ss") %></td>
                                        <td style="width: 10%;">
                                            <asp:LinkButton ID="linAmend" runat="server" Text="修改" OnClientClick='<%# "showEditUser(" + Eval("id") + ");" %>'></asp:LinkButton>
                                            &nbsp;<asp:LinkButton ID="lnkDelete" runat="server" Text="删除" CommandName='<%# Eval("id") %>' Visible='<%# getVisible(Eval("name")) %>' OnClientClick="return confirm('确定要删除吗？删除后不可恢复');" OnClick="lnkDelete_Click"></asp:LinkButton>
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
                    <RowStyle BackColor="white" CssClass="gv_row"/>
                </asp:GridView>
            </ContentTemplate>
        </asp:UpdatePanel>
    </div>
</asp:Content>
