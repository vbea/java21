<%@ Page Title="" Language="C#" MasterPageFile="~/Java.Master" AutoEventWireup="true" CodeBehind="Feedback.aspx.cs" Inherits="Java21.Net.Feedback" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <title>反馈</title>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div style="width: 100%; height: 100%; padding: 10px; margin: 0 auto;">
        <asp:GridView ID="gvFeed" runat="server" AutoGenerateColumns="False" Width="80%" AllowPaging="True" OnPageIndexChanging="gvFeed_PageIndexChanging" CssClass="grid_b" GridLines="None" ShowHeaderWhenEmpty="True">
            <Columns>
                <asp:TemplateField>
                    <HeaderTemplate>
                        <span>反馈记录</span>
                    </HeaderTemplate>
                    <ItemTemplate>
                        <table style="width: 100%; text-align: left;">
                            <tr>
                                <td style="width: 60%; color:gray;"><%# Eval("id") + "：" + Convert.ToDateTime(Eval("cdate")).ToString("yyyy-MM-dd HH:mm:ss") %></td>
                                <td style="width: 30%; text-align:right; padding-right:5px;">
                                    <asp:LinkButton ID="linkDelete" runat="server" CommandName='<%# Eval("id") %>' OnClick="linkDelete_Click" OnClientClick="return confirm('确定要删除吗？');">删除</asp:LinkButton></td>
                             </tr>
                            <tr>
                                <td colspan="2" style="width: 99%; padding:5px;"><%# Eval("suggest") %></td>
                            </tr>
                            <tr>
                                <td style="width: 50%; color:gray;"><%# "D:" + Eval("device") %></td>
                                <td style="width: 50%; color:gray;"><%# "C:" + Eval("contact") %></td>
                            </tr>
                        </table>
                    </ItemTemplate>
                </asp:TemplateField>
            </Columns>
            <EmptyDataTemplate>
                <div style="width: 100%; text-align: center;">当前没有记录</div>
            </EmptyDataTemplate>
            <FooterStyle BackColor="#4780AE" Font-Bold="True" ForeColor="White" />
            <HeaderStyle BackColor="#4780AE" Font-Bold="True" ForeColor="White" CssClass="gv_hrader" />
            <PagerStyle CssClass="pager_in" />
            <RowStyle BackColor="white" CssClass="gv2_row" />
        </asp:GridView>
    </div>
</asp:Content>
