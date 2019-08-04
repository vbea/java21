<%@ Page Title="" Language="C#" MasterPageFile="~/Java.Master" AutoEventWireup="true" CodeBehind="JavaKeys.aspx.cs" Inherits="Java21.Net.JavaKeys" %>

<asp:Content ID="Content1" ContentPlaceHolderID="head" runat="server">
    <title>注册码</title>
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="ContentPlaceHolder1" runat="server">
    <div style="width: 100%; height: 100%; padding: 0px; margin: 0 auto;">
        <p>21天学通Java注册码列表</p>
        <asp:GridView ID="gvKey" runat="server" AutoGenerateColumns="False" Width="100%" AllowPaging="True" OnPageIndexChanging="gvKey_PageIndexChanging" CssClass="grid_b" GridLines="None">
            <AlternatingRowStyle BackColor="#E7F7FF" />
            <Columns>
                <asp:TemplateField>
                    <HeaderTemplate>
                        <table style="width:100%; text-align:center;">
                            <tr>
                                <td style="width: 5%;">ID</td>
                                <td style="width: 25%;">注册码</td>
                                <td style="width: 7%;">已使用</td>
                                <td style="width: 8%;">最大次数</td>
                                <td style="width: 10%;">版本</td>
                                <td style="width: 10%;">生成时间</td>
                                <td style="width: 10%;">到期时间</td>
                                <td style="width: 20%;">说明</td>
                                <td style="width: 5%;">操作</td>
                            </tr>
                        </table>
                    </HeaderTemplate>
                    <ItemTemplate>
                        <table style="width:100%; text-align:center;">
                            <tr>
                                <td style="width: 5%;"><%# Eval("id") %></td>
                                <td style="width: 25%;"><%# showKeys(Eval("keys").ToString()) %></td>
                                <td style="width: 7%;"><%# Eval("curr") %></td>
                                <td style="width: 8%;"><%# Eval("maxc") %></td>
                                <td style="width: 10%;"><%# getVersion(Eval("ver")) %></td>
                                <td style="width: 10%;"><%# Convert.ToDateTime(Eval("cdate")).ToString("yyyy-MM-dd") %></td>
                                <td style="width: 10%;"><%# Convert.ToDateTime(Eval("cdate")).AddMonths(1).ToString("yyyy-MM-dd") %></td>
                                <td style="width: 20%;"><%# Eval("mark") %></td>
                                <td style="width: 5%;">
                                    <asp:LinkButton ID="lnkDelete" runat="server" Text="删除" CommandName='<%# Eval("id") %>' Visible='<%# role==1 %>' OnClientClick="return confirm('确定要删除吗？删除后不可恢复');" OnClick="lnkDelete_Click"></asp:LinkButton>
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
            <HeaderStyle BackColor="#4780AE" Font-Bold="True" ForeColor="White" CssClass="gv_hrader"/>
            <PagerStyle CssClass="pager_in" />
            <RowStyle BackColor="white" CssClass="gv_row"/>
        </asp:GridView>
    </div>
</asp:Content>
