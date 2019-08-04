<%@ Page Title="" Language="C#" MasterPageFile="~/Java.Master" AutoEventWireup="true" CodeBehind="Recycle.aspx.cs" Inherits="Java21.Net.Recycle" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <title>回收站</title>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <table style="width:100%;">
        <tr>
            <td>回收站
            </td>
            <td style="text-align:right;">
                <asp:LinkButton ID="linkClear" runat="server" OnClick="linkClear_Click" Visible="false" OnClientClick="return confirm('确定要清空回收站？');">清空回收站</asp:LinkButton><asp:Literal ID="litCount" runat="server"></asp:Literal></td>
        </tr>
    </table>
    <asp:GridView ID="gvRecycle" runat="server" AutoGenerateColumns="False" Width="100%" AllowPaging="True" OnPageIndexChanging="gvRecycle_PageIndexChanging" CssClass="grid_b" GridLines="None">
            <AlternatingRowStyle BackColor="#E7F7FF" />
            <Columns>
                <asp:TemplateField>
                    <HeaderTemplate>
                        <table style="width:100%; text-align:center;">
                            <tr>
                                <td style="width: 5%;">ID</td>
                                <td style="width: 20%;">标题</td>
                                <td style="width: 30%;">内容</td>
                                <td style="width: 8%;">创建者</td>
                                <td style="width: 10%;">创建时间</td>
                                <td style="width: 8%;">上次修改</td>
                                <td style="width: 10%;">修改时间</td>
                                <td style="width: 9%;">操作</td>
                            </tr>
                        </table>
                    </HeaderTemplate>
                    <ItemTemplate>
                        <table style="width:100%; text-align:center;">
                            <tr>
                                <td style="width: 5%;"><%# Eval("id") %></td>
                                <td style="width: 20%;"><%# Eval("title") %></td>
                                <td style="width: 30%;"><%# StringTruncat(Eval("artical").ToString(),20,"...") %></td>
                                <td style="width: 8%;"><%# Eval("cuser") %></td>
                                <td style="width: 10%;"><%# Convert.ToDateTime(Eval("cdate")).ToString("yyyy-MM-dd") %></td>
                                <td style="width: 8%;"><%# Eval("euser") %></td>
                                <td style="width: 10%;"><%# Convert.ToDateTime(Eval("edate")).ToString("yyyy-MM-dd") %></td>
                                <td style="width: 9%;">
                                    <asp:LinkButton ID="lnkRestore" runat="server" Text="恢复" CommandName='<%# Eval("id") %>' OnClientClick="return confirm('确定要恢复吗？');" OnClick="lnkRestore_Click"></asp:LinkButton>
                                    <asp:LinkButton ID="lnkDelete" runat="server" Text="删除" CommandName='<%# Eval("id") %>' OnClientClick="return confirm('确定要删除吗？删除后不可恢复');" OnClick="lnkDelete_Click"></asp:LinkButton>
                                </td>
                            </tr>
                        </table>
                    </ItemTemplate>
                </asp:TemplateField>
            </Columns>
            <EmptyDataTemplate>
                <div style="width: 100%; text-align: center;">已清空回收站</div>
            </EmptyDataTemplate>
            <FooterStyle CssClass="pager_in" />
            <HeaderStyle BackColor="#4780AE" Font-Bold="True" ForeColor="White" CssClass="gv_hrader"/>
            <PagerStyle CssClass="pager_in" />
            <RowStyle BackColor="white" CssClass="gv_row"/>
        </asp:GridView>
</asp:Content>
