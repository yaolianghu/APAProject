using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Data;
using MySql.Data.MySqlClient;
using System.Diagnostics;
using System.Drawing;
using System.Text.RegularExpressions;
using System.IO;
using System.Threading;
using Google.GData.Analytics;
using Google.GData.Client;
using Google.GData.Extensions;
using System.Collections;
using System.Web.UI.HtmlControls;

public partial class _Default : System.Web.UI.Page
{
    protected void Page_Load(object sender, EventArgs e)
    {
        Log.Info("Paqe Load!");
        if (!IsPostBack)
        {
            pageSearchTextBox.Visible = false;
            pageSearchButton.Visible = false;
            Label5.Visible = false;

            keywordSearchButton.Visible = false;
            pageURLTextBox.Visible = false;
            KeywordListBox.Visible = false;

            DeleteKeywordButton.Visible = false;

            addKeywordTextBox.Visible = false;
            addKeywordDescriptionTextBox.Visible = false;
            addKeywordButton.Visible = false;
            addKeywordLabel.Visible = false;
            addKeywordDescriptionLabel.Visible = false;
            updateKeywordButton.Visible = false;

            Label6.Visible = false;
            Label7.Visible = false;
            StartDateTextBox.Visible = false;
            EndDateTextBox.Visible = false;
            BindDateDropDownList.Visible = false;
            DateSearchButton.Visible = false;

            AnalyticsButton.Visible = false;

            PageAttributeShowCheckBox.Visible = false;
            PageShowCheckBox.Visible = false;
            PageAttributeAffiliationCheckBox.Visible = false;
            PageKeywordCheckBox.Visible = false;
            PageKeywordAffiliationCheckBox.Visible = false;
            GoogleCheckBox.Visible = false;
        }

        Label1.Visible = false;
        Label2.Visible = false;
        //Label4.Visible = false;
        //Label3.Visible = false;
        Label8.Visible = false;
        Label9.Visible = false;
        Label10.Visible = false;
        JobMessage.Visible = false;

        InitializeDB();

    }

    private MySqlConnection conn;
    private string server;
    private string database;
    private string uid;
    private string password;
    public log4net.ILog Log = log4net.LogManager.GetLogger(System.Reflection.MethodBase.GetCurrentMethod().DeclaringType);

    /// <summary>
    /// Pre-information needs to connect with MySQL.
    /// </summary>
    private void InitializeDB()
    {
        Log.Debug("InitializeDB method is called!");
        server = "172.16.1.38";
        database = "RSDatabase";
        uid = "hu";
        password = "rstransit12!";
        string connectionStr;
        connectionStr = "Server=" + server + ";" + "Database=" + database + ";" + "uid=" + uid + ";" + "password=" + password + ";";
        conn = new MySqlConnection(connectionStr);
        Log.Debug("InitializedDB method is done!");
    }

    /// <summary>
    /// Create a connection to database
    /// </summary>
    /// <returns>True or False</returns>
    private bool OpenConnection()
    {
        Log.Debug("OpenConnection method is called!");
        try
        {    
            conn.Open();
            Log.Debug("OpenConnection method is done!");
            return true;
        }
        catch (MySqlException ex)
        {
            Log.Error(ex.ToString());
            return false;
        }
    }

    /// <summary>
    /// Close connection
    /// </summary>
    private void CloseConnection()
    {
        Log.Debug("CloseConnection method is done!");
        try
        {
            conn.Close();
            Log.Debug("CloseConnection method is done!");
        }
        catch (MySqlException ex)
        {
            Log.Error(ex.ToString());
        }
    }

    /// <summary>
    /// Get data from database
    /// </summary>
    /// <param name="selectStatement">SQL Query</param>
    /// <returns>queried data set</returns>
    protected DataSet GetDataset(string selectStatement) 
    {
        Log.Debug("GetDataset Method is called!");
        MySqlDataAdapter adapter = new MySqlDataAdapter(selectStatement, conn);
        DataSet dateSet = new DataSet();
        try
        {
            adapter.Fill(dateSet);
        }
        catch (Exception ex)
        {
            //Label2.Visible = true; 
            //Label2.Text = ex.ToString();
            Log.Error(ex.ToString());
        }
        this.CloseConnection();
        Log.Debug("GetDataset Method is done!");
        return dateSet;
    }

    /// <summary>
    /// Create an asp pages' tree view
    /// </summary>
    protected void Load_Tree()
    {
        Log.Debug("Load_Tree Method is called!");
        DataSet directoryDataset = GetDataset("SELECT * FROM RSDirectory");
        PageTreeView.Nodes.Clear();
        TreeNode tnParent = new TreeNode();
        string value = null;
        foreach (DataRow dr in directoryDataset.Tables[0].Rows)
        {
            if ((int)dr["ParentID"] == 0)
            {
                tnParent.Text = dr["RSDirectoryName"].ToString();
                value = dr["RSDirectoryId"].ToString();
                tnParent.ImageUrl = "~/img/folderIcon.gif";
                tnParent.Expanded = false;
                PageTreeView.Nodes.Add(tnParent);
                FillChild(tnParent, value);
            }
        }
        Log.Debug("Load_Tree Method is done!");
    }

    /// <summary>
    /// recusive add a sub-folder under root node
    /// </summary>
    /// <param name="parent">rootNode</param>
    /// <param name="parentId">parentId</param>
    protected void FillChild(TreeNode parent, string parentId)
    {
        Log.Debug("FillChild method is called!");
        string query = "SELECT RSDirectoryName, RSDirectoryId FROM RSDirectory WHERE ParentID =" + parentId;
        DataSet directoryDataset = GetDataset(query);

        if ((directoryDataset.Tables[0].Rows.Count > 0) && ExistsASPPage(parentId))
        {
            foreach (DataRow dr in directoryDataset.Tables[0].Rows)
            {
                TreeNode childNode = new TreeNode();
                childNode.Text = dr["RSDirectoryName"].ToString().Trim();
                string directoryId = dr["RSDirectoryId"].ToString();
                childNode.ImageUrl = "~/img/folderIcon.gif";
                childNode.Collapse();

                if (ExistsASPPage(directoryId))
                {
                    parent.ChildNodes.Add(childNode);
                }
                FillChild(childNode, directoryId);
            }
        }
        Log.Debug("FillChild method is done!");
    }

    /// <summary>
    /// Check if the folder contains ASP pages.
    /// </summary>
    /// <param name="directoryId">directoryId</param>
    /// <returns>True or False</returns>
    protected bool ExistsASPPage(string directoryId)
    {
        Log.Debug("ExistsASPPage method is called!");
        string query = "select RSPagename, RSPageid from RSPage where RSDirectoryid =" + directoryId;
        DataSet pageDateSet = GetDataset(query);
        if (pageDateSet.Tables[0].Rows.Count > 0)
        {
            Log.Debug("ExistsASPPage method is done!");
            return true;
        }
        else
        {
            Log.Debug("ExistsASPPage method is done!");
            return false;
        }
    }

    /// <summary>
    /// load tree click button
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    protected void LoadTreeButton_Click(object sender, EventArgs e)
    {
        Log.Info("LoadTreeButton_Click method is called!");
        PageShowCheckBox.Visible = false;
        PageAttributeShowCheckBox.Visible = false;
        PageAttributeAffiliationCheckBox.Visible = false;
        PageKeywordCheckBox.Visible = false;
        PageKeywordAffiliationCheckBox.Visible = false;
        GoogleCheckBox.Visible = false;

        pageSearchButton.Visible = true;
        pageSearchTextBox.Visible = true;
        Label5.Visible = true;

        AnalyticsButton.Visible = true;

        Label6.Visible = true;
        Label7.Visible = true;
        StartDateTextBox.Visible = true;
        EndDateTextBox.Visible = true;
        BindDateDropDownList.Visible = true;
        DateSearchButton.Visible = true;
        Label8.Visible = true;
        Label8.Text = "The date format is \"yyyy-MM-dd\".";

        Load_Tree();
        JobMessage.Visible = true;
        JobMessage.Text = "Load Successfully!";

        PageGridView.DataSource = null;
        PageGridView.DataBind();

        PageAttributeSummaryGridView.DataSource = null;
        PageAttributeSummaryGridView.DataBind();

        PageAttributeAffiliationGridView.DataSource = null;
        PageAttributeAffiliationGridView.DataBind();

        PageKeywordSummaryGridView.DataSource = null;
        PageKeywordSummaryGridView.DataBind();

        PageKeywordAffiliationGridView.DataSource = null;
        PageKeywordAffiliationGridView.DataBind();

        AnalyticsGridView.DataSource = null;
        AnalyticsGridView.DataBind();

        keywordSearchButton.Visible = false;
        pageURLTextBox.Visible = false;
        KeywordListBox.Visible = false;
        DeleteKeywordButton.Visible = false;

        addKeywordTextBox.Visible = false;
        addKeywordButton.Visible = false;
        updateKeywordButton.Visible = false;
        addKeywordDescriptionTextBox.Visible = false;
        addKeywordLabel.Visible = false;
        addKeywordDescriptionLabel.Visible = false;

        Log.Info("LoadTreeButton_Click method is done!");
    }

    /// <summary>
    /// Run the java job to update the whole database
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    protected void RunJob_Click(object sender, EventArgs e)
    {
        Log.Info("RunJob_Click method is called!");
        
        string jarFilePath = HttpContext.Current.Request.MapPath("~/3Party/RSAPARunnable_UpdateNew.jar");
        //JobMessage.Visible = true;
        //JobMessage.Text = jarFilePath;
        Process process = new Process();
        try
        {
            foreach (Process proc in Process.GetProcessesByName("java"))
            {
                proc.Kill();
                Log.Info("The old java.exe process is killed.!");
            }

            process.StartInfo.UseShellExecute = false;
            process.StartInfo.FileName = "java.exe";
            process.StartInfo.Arguments = "-jar " + '"' + jarFilePath;
            //process.StartInfo.CreateNoWindow = true;
            process.Start();
            //JobMessage.Visible = true;
            //JobMessage.Text = "Job executes successfully!";
        }
        catch (Exception ex)
        {
            Log.Error(ex.ToString());
        }
    }

    /// <summary>
    /// Click the node displays the asp pages in this folder.
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    protected void PageTreeView_SelectedNodeChanged(object sender, EventArgs e)
    {
        Log.Info("PageTreeView_SelectedNodeChanged method is called!");
        PageShowCheckBox.Visible = true;
        PageAttributeShowCheckBox.Visible = false;
        PageAttributeAffiliationCheckBox.Visible = false;
        PageKeywordCheckBox.Visible = false;
        PageKeywordAffiliationCheckBox.Visible = false;
        GoogleCheckBox.Visible = false;

        Label1.Text = "";
        Label2.Text = "";
        //Label3.Text = "";
        //Label4.Text = "";

        pageURLTextBox.Visible = true;
        KeywordListBox.Visible = true;
        keywordSearchButton.Visible = true;
        pageURLTextBox.Text = "";
        BindKeywordListBox();
                
        addKeywordDescriptionTextBox.Visible = true;
        addKeywordTextBox.Visible = true;   
        addKeywordLabel.Visible = true;
        addKeywordDescriptionLabel.Visible = true;
        addKeywordButton.Visible = true;
        updateKeywordButton.Visible = true;
        DeleteKeywordButton.Visible = true;

        string absolutePath = @"\\\\testweb\\Inetpub\\";
        //to get the full path name of the node
        absolutePath = absolutePath + PageTreeView.SelectedNode.ValuePath;
        //C# query the data from MySQL needs "\\\\" rather than "\\". Eg: select * from rspage where url = 'z:\\\\wwwroot\\\\....'
        absolutePath = absolutePath.Replace(@"/", @"\\");

        this.PageGridView.SelectedIndex = -1;
        this.PageAttributeSummaryGridView.SelectedIndex = -1;

        PageGridView.DataSource = null;
        PageGridView.DataBind();

        PageAttributeSummaryGridView.DataSource = null;
        PageAttributeSummaryGridView.DataBind();

        PageAttributeAffiliationGridView.DataSource = null;
        PageAttributeAffiliationGridView.DataBind();

        PageKeywordSummaryGridView.DataSource = null;
        PageKeywordSummaryGridView.DataBind();

        PageKeywordAffiliationGridView.DataSource = null;
        PageKeywordAffiliationGridView.DataBind();

        AnalyticsGridView.DataSource = null;
        AnalyticsGridView.DataBind();

        PageGridView.PageIndex = 0;

        GetSelectedNodeData(absolutePath);
        Label9.Visible = true;
        Label9.Text = "Total Number of Pages: " + pageGridViewList.Count.ToString();

        //Resolve on click the same node not fire the SelectNodeChanged method.
        PageTreeView.SelectedNode.Selected = false;

        Log.Info("PageTreeView_SelectedNodeChanged method is done!");
    }

    private static List<PageGridView_Client> pageGridViewList = new List<PageGridView_Client>();

    /// <summary>
    /// Get the selected node data
    /// </summary>
    /// <param name="absolutePath">Absolute Path</param>
    protected void GetSelectedNodeData(string absolutePath)
    {
        Log.Debug("GetSelectedNodeData method is called!");
        pageGridViewList = SelectPageInfoByDirectoryAbsolutePath(absolutePath);
        PageGridView.DataSource = pageGridViewList;
        PageGridView.DataBind();
        Log.Debug("GetSelectedNodeData method is done!");
    }

    /// <summary>
    /// Get page info by using directory absolute path 
    /// </summary>
    /// <param name="absolutePath">Absolute Path</param>
    /// <returns>PageGridViewList</returns>
    protected List<PageGridView_Client> SelectPageInfoByDirectoryAbsolutePath(string absolutePath)
    {
        Log.Debug("SelectPageInfoByDirectoryAbsolutePath method is called!");
        string query = "SELECT * FROM RSPage a JOIN RSDirectory b ON a.RSDirectoryId = b.RSDirectoryId WHERE absolutePath = '" + absolutePath + "'";
        List<PageGridView_Client> listPageInfoByDirectoryName = new List<PageGridView_Client>();

        if (this.OpenConnection() == true)
        {
            MySqlCommand cmd = new MySqlCommand(query, conn);
            MySqlDataReader dataReader = cmd.ExecuteReader();

            while (dataReader.Read())
            {
                listPageInfoByDirectoryName.Add(new PageGridView_Client(dataReader["url"] + "", dataReader["RSPageCreateDate"] + "", 
                    dataReader["RSPageModifiedDate"] + "", dataReader["RSPageLastAccessDate"] + ""));
            }

            dataReader.Close();

            this.CloseConnection();

            Log.Debug("SelectPageInfoByDirectoryAbsolutePath method is done!");

            return listPageInfoByDirectoryName;
        }
        else
        {
            Log.Debug("SelectPageInfoByDirectoryAbsolutePath method is done!");
            return listPageInfoByDirectoryName;
        }
    }

    private static List<PageAttributeSummaryGridView_Client> pageAttributeSummaryGridViewList = new List<PageAttributeSummaryGridView_Client>();

    /// <summary>
    /// Select Index Changed
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    protected void PageGridView_SelectedIndexChanged(Object sender, EventArgs e)
    {
        Log.Info("PageGridView_SelectedIndexChanged method is called!");
        PageAttributeShowCheckBox.Visible = true;
        PageAttributeAffiliationCheckBox.Visible = false;
        PageKeywordCheckBox.Visible = false;
        PageKeywordAffiliationCheckBox.Visible = false;
        GoogleCheckBox.Visible = false;

        GridViewRow selectedPageName = PageGridView.SelectedRow;
        this.PageAttributeSummaryGridView.SelectedIndex = -1;

        Label2.Visible = true;
        Label2.Text = "\"" + selectedPageName.Cells[1].Text + "\"";
        pageURLTextBox.Text = selectedPageName.Cells[1].Text;

        string url = selectedPageName.Cells[1].Text;
        url = url.Replace(@"\", @"\\");
        if (url.Contains("&#39;"))
        {
            url = url.Replace(@"&#39;", @"\'");
        }
        pageAttributeSummaryGridViewList = selectPageInfoByURL(url);

        if (pageAttributeSummaryGridViewList.Count != 0)
        {
            PageAttributeSummaryGridView.DataSource = pageAttributeSummaryGridViewList;
            PageAttributeSummaryGridView.DataBind();

            AnalyticsGridView.DataSource = null;
            AnalyticsGridView.DataBind();

            PageAttributeAffiliationGridView.DataSource = null;
            PageAttributeAffiliationGridView.DataBind();

            PageKeywordSummaryGridView.DataSource = null;
            PageKeywordSummaryGridView.DataBind();

            PageKeywordAffiliationGridView.DataSource = null;
            PageKeywordAffiliationGridView.DataBind();    
        }
        else
        {
            Label2.Visible = true; 
            Label2.Text = "There is no reference attributes in \"" + Label2.Text + "\" page!";

            AnalyticsGridView.DataSource = null;
            AnalyticsGridView.DataBind();

            PageAttributeSummaryGridView.DataSource = null;
            PageAttributeSummaryGridView.DataBind();

            PageAttributeAffiliationGridView.DataSource = null;
            PageAttributeAffiliationGridView.DataBind();

            PageKeywordSummaryGridView.DataSource = null;
            PageKeywordSummaryGridView.DataBind();

            PageKeywordAffiliationGridView.DataSource = null;
            PageKeywordAffiliationGridView.DataBind();
        }
        Log.Info("PageGridView_SelectedIndexChanged method is done!");
    }

    /// <summary>
    /// Page Index Changing
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    protected void PageGridView_PageIndexChanging(object sender, GridViewPageEventArgs e)
    {
        Log.Info("PageGridView_PageIndexChanging method is called!");
        this.PageGridView.SelectedIndex = -1;

        PageAttributeShowCheckBox.Visible = false;
        PageAttributeAffiliationCheckBox.Visible = false;
        PageKeywordCheckBox.Visible = false;
        PageKeywordAffiliationCheckBox.Visible = false;
        GoogleCheckBox.Visible = false;

        PageGridView.PageIndex = e.NewPageIndex;
        PageGridView.DataSource = pageGridViewList;
        PageGridView.DataBind();

        AnalyticsGridView.DataSource = null;
        AnalyticsGridView.DataBind();

        PageAttributeSummaryGridView.DataSource = null;
        PageAttributeSummaryGridView.DataBind();

        PageAttributeAffiliationGridView.DataSource = null;
        PageAttributeAffiliationGridView.DataBind();

        PageKeywordSummaryGridView.DataSource = null;
        PageKeywordSummaryGridView.DataBind();

        PageKeywordAffiliationGridView.DataSource = null;
        PageKeywordAffiliationGridView.DataBind();

        Log.Info("PageGridView_PageIndexChanging method is done!");
    }
    
    /// <summary>
    /// Show or hide the page table
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    public void PageShowDiv(object sender, System.EventArgs e)
    {
        Label2.Visible = true;
        //Label4.Visible = true;
        Label1.Visible = true;
        //Label3.Visible = true;
        //Label10.Visible = true;
        var div = expandGridView1 as HtmlGenericControl;
        var checkbox = sender as CheckBox;

        if (checkbox.Checked == true)
        {
            div.Style.Clear();
            div.Style.Add("display", "none");
        }
        else
        {
            div.Style.Clear();
            div.Style.Add("display", "block");
        }
    }

    /// <summary>
    /// Show or hide the page attribute table
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    public void PageAttributeShowDiv(object sender, System.EventArgs e)
    {
        Label2.Visible = true;
        //Label4.Visible = true;
        
        var div = expandGridView2 as HtmlGenericControl;
        var checkbox = sender as CheckBox;

        if (checkbox.Checked == true)
        {
            div.Style.Clear();
            div.Style.Add("display", "none");
        }
        else
        {
            div.Style.Clear();
            div.Style.Add("display", "block");
        }
    }

    /// <summary>
    /// Show or hide page attribute affiliation table
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    public void PageAttributeAffiliationShowDiv(object sender, System.EventArgs e)
    {
        Label2.Visible = true;
        //Label4.Visible = true;

        var div = expandGridView3 as HtmlGenericControl;
        var checkbox = sender as CheckBox;

        if (checkbox.Checked == true)
        {
            div.Style.Clear();
            div.Style.Add("display", "none");
        }
        else
        {
            div.Style.Clear();
            div.Style.Add("display", "block");
        }
    }

    /// <summary>
    /// Show or hide keyword table
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    public void PageKeywordShowDiv(object sender, System.EventArgs e)
    {
        Label1.Visible = true;
        //Label3.Visible = true;
        var div = expandGridView4 as HtmlGenericControl;
        var checkbox = sender as CheckBox;

        if (checkbox.Checked == true)
        {
            div.Style.Clear();
            div.Style.Add("display", "none");
        }
        else
        {
            div.Style.Clear();
            div.Style.Add("display", "block");
        }
    }

    /// <summary>
    /// Show or hide page keyword affiliation table
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    public void PageKeyordAffiliationCheckBoxShowDiv(object sender, System.EventArgs e)
    {
        Label1.Visible = true;
        //Label3.Visible = true;
        var div = expandGridView5 as HtmlGenericControl;
        var checkbox = sender as CheckBox;

        if (checkbox.Checked == true)
        {
            div.Style.Clear();
            div.Style.Add("display", "none");
        }
        else
        {
            div.Style.Clear();
            div.Style.Add("display", "block");
        }
    }

    /// <summary>
    /// Show or hide google analytics information table
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    public void GoogleShowDiv(object sender, System.EventArgs e)
    {
        Label2.Visible = true;
        //Label3.Visible = true;
        //Label4.Visible = true;
        //Label1.Visible = true;
        var div = expandGridView6 as HtmlGenericControl;
        var checkbox = sender as CheckBox;

        if (checkbox.Checked == true)
        {
            div.Style.Clear();
            div.Style.Add("display", "none");
        }
        else
        {
            div.Style.Clear();
            div.Style.Add("display", "block");
        }
    }

    /// <summary>
    /// Click button to bind analytics grid view
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    protected void AnalyticsButton_Click(object sender, EventArgs e)
    {
        if (String.IsNullOrEmpty(pageURLTextBox.Text))
        {
            Label8.Visible = true;
            Label8.Text = "Please select one page!";
            GoogleCheckBox.Visible = false;
            PageKeywordCheckBox.Visible = false;
        }
        else
        {
            GoogleCheckBox.Visible = true;
            string url = "";
            url = pageURLTextBox.Text;
            url = url.Replace("\\\\testweb\\inetpub\\wwwroot\\", "/");
            url = url.Replace("\\", "/");
            List<AnalyticsGridView_Client> AnalyticsList = new List<AnalyticsGridView_Client>();
            AnalyticsList = GetReportData(url);

            if (AnalyticsList.Count > 0)
            {
                AnalyticsGridView.DataSource = AnalyticsList;
                AnalyticsGridView.DataBind();

                Label2.Visible = true;
                //Label4.Visible = true;
            }
            else
            {
                Label10.Visible = true;
                Label10.Text = "There is no reference information in Google Analytics Tool!";
                Label2.Visible = true;
                //Label4.Visible = true;
            }
        }
    }

    /// <summary>
    /// Get the Google AnalyticsService
    /// </summary>
    /// <returns></returns>
    protected AnalyticsService AccountInfo()
    {
        string email = null;
        string pass = null;

        email = "yaolianghu1026@gmail.com";
        pass = "5t6y7u8i9o";

        AnalyticsService service = new AnalyticsService("WebApp");
        service.setUserCredentials(email, pass);
        return service;
    }

    /// <summary>
    /// Get the report data
    /// </summary>
    /// <param name="pageURL"></param>
    /// <returns></returns>
    protected List<AnalyticsGridView_Client> GetReportData(string pageURL)
    {
        string gkey = "key=AIzaSyBjA_dnNZjAI3m8Z9cBpbJk2RPktBh3SQc";
        string dataFeedUrl = "https://www.googleapis.com/analytics/v2.4/data?" + gkey;
        List<AnalyticsGridView_Client> reportList = new List<AnalyticsGridView_Client>();
        try
        {
            AnalyticsService service = new AnalyticsService("WebApp");
            service = AccountInfo();

            DataQuery dataQuery = new DataQuery(dataFeedUrl);

            dataQuery.Ids = "ga:63890331";
            dataQuery.Dimensions = "ga:pagePath,ga:date";
            dataQuery.Metrics = "ga:pageviews,ga:uniquePageviews,ga:avgTimeOnPage";

            //new DateTime(2013, 8, 5).ToString("yyyy-MM-dd")
            string GAStartDate = DateTime.Now.AddMonths(-1).ToString("yyyy-MM-dd");
            string GAEndDate = DateTime.Now.AddDays(-1).ToString("yyyy-MM-dd");

            dataQuery.GAStartDate = GAStartDate;
            dataQuery.GAEndDate = GAEndDate;
            dataQuery.StartIndex = 1;
            dataQuery.Filters = "ga:pagePath==" + pageURL;
            
            DataFeed dataFeedVisits = service.Query(dataQuery);
            //int totalResult = dataFeedVisits.TotalResults;

            foreach (DataEntry entry in dataFeedVisits.Entries)
            {
                reportList.Add(new AnalyticsGridView_Client(entry.Dimensions[0].Value, entry.Dimensions[1].Value, entry.Metrics[0].Value,
                    entry.Metrics[1].Value, entry.Metrics[2].Value));
            }

            return reportList;            
        }
        catch (Exception ex)
        {
            Label1.Text = ex.ToString();
            return reportList;
        }
    }

    /// <summary>
    /// Select page information by using URL
    /// </summary>
    /// <param name="url">url</param>
    /// <returns>PageAttributeSummaryGridViewList</returns>
    protected List<PageAttributeSummaryGridView_Client> selectPageInfoByURL(string url)
    {
        Log.Debug("selectPageInfoByURL method is called!");
        string query = @"SELECT RSPageName, RSAttributeName, count(*) as totalNumber 
                         From RSPage a join RSPageAttributeAffiliation b on a.RSPageid = b.RSPageid join RSAttribute c on b.RSAttributeid = c.RSAttributeid    
                         where url = '" + url + "'" +
                         "group by b.RSAttributeid";

        //Create a list to store the result
        List<PageAttributeSummaryGridView_Client> pageInfoList = new List<PageAttributeSummaryGridView_Client>();

        if (this.OpenConnection() == true)
        {
            MySqlCommand cmd = new MySqlCommand(query, conn);
            MySqlDataReader dataReader = cmd.ExecuteReader();

            while (dataReader.Read())
            {
                pageInfoList.Add(new PageAttributeSummaryGridView_Client(dataReader["RSPageName"] + "", dataReader["RSAttributeName"] + "", dataReader["totalNumber"] + ""));
            }

            dataReader.Close();
            this.CloseConnection();
            Log.Debug("selectPageInfoByURL method is done!");
            return pageInfoList;
        }
        else
        {
            Log.Debug("selectPageInfoByURL method is done!");
            return pageInfoList;
        }
    }

    private static List<PageAttributeAffiliationGridView_Client> pageAttributeAffiliationGridViewList = new List<PageAttributeAffiliationGridView_Client>();

    /// <summary>
    /// Selected Index Changed
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    protected void PageAttributeSummerGridView_SelectedIndexChanged(Object sender, EventArgs e)
    {
        Log.Info("PageAttributeSummerGridView_SelectedIndexChanged method is called!");
        PageAttributeAffiliationCheckBox.Visible = true;
        PageAttributeAffiliationGridView.PageIndex = 0;
        GridViewRow row = PageGridView.SelectedRow;

        //Label4.Visible = true;
        //Label4.Text = "The \"" + PageAttributeSummaryGridView.SelectedRow.Cells[2].Text + "\" in the \"" + row.Cells[1].Text + "\" page.";
        Label2.Visible = true;
        Label2.Text = "\"" + row.Cells[1].Text + "\"";
        string url = row.Cells[1].Text;

        url = url.Replace(@"\", @"\\");

        if (url.Contains("&#39;"))
        {
            url = url.Replace(@"&#39;", @"\'");
        }
        string RSAttributeName = PageAttributeSummaryGridView.SelectedRow.Cells[2].Text;
        pageAttributeAffiliationGridViewList = selectPageInfoByURLAndAttributeName(url, RSAttributeName);

        if (pageAttributeAffiliationGridViewList.Count != 0)
        {
            PageAttributeAffiliationGridView.DataSource = pageAttributeAffiliationGridViewList;
            PageAttributeAffiliationGridView.DataBind();                        
        }
        else
        {
            PageAttributeAffiliationGridView.DataSource = null;
            PageAttributeAffiliationGridView.DataBind();
        }
        Log.Info("PageAttributeSummerGridView_SelectedIndexChanged method is done!");
    }

    /// <summary>
    /// Page Index Changing
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    protected void PageAttributeAffiliationGridView_PageIndexChanging(object sender, GridViewPageEventArgs e)
    {
        Log.Info("PageAttributeAffiliationGridView_PageIndexChanging method is called!");
        GridViewRow row = PageGridView.SelectedRow;
        //Label4.Visible = true;
        //Label4.Text = "The \"" + PageAttributeSummaryGridView.SelectedRow.Cells[2].Text + "\" in the \"" + row.Cells[1].Text + "\" page.";
        Label2.Visible = true;
        Label2.Text = "\"" + row.Cells[1].Text + "\"";

        PageAttributeAffiliationGridView.PageIndex = e.NewPageIndex;

        PageAttributeAffiliationGridView.DataSource = pageAttributeAffiliationGridViewList;

        PageAttributeAffiliationGridView.DataBind();

        PageKeywordSummaryGridView.DataSource = null;

        PageKeywordSummaryGridView.DataBind();
        Log.Info("PageAttributeAffiliationGridView_PageIndexChanging method is done!");
    }

    /// <summary>
    /// Select page information by using url and attribute name.
    /// </summary>
    /// <param name="URL">URL</param>
    /// <param name="RSAttributeName">RSAttributeName</param>
    /// <returns>pageAttributeSummerGridViewList</returns>
    protected List<PageAttributeAffiliationGridView_Client> selectPageInfoByURLAndAttributeName(string URL, string RSAttributeName)
    {
        Log.Debug("selectPageInfoByURLAndAttributeName method is called!");
        string query = @"SELECT RSPageAttributeAffiliationDescription, RSPageAttributeAffiliationSector
                         From RSPage a join RSPageAttributeAffiliation b on a.RSPageid = b.RSPageid join RSAttribute c on b.RSAttributeid = c.RSAttributeid
                         where url = '" + URL + "' and RSAttributeName = '" + RSAttributeName + "'";

        List<PageAttributeAffiliationGridView_Client> pageInfoByURLAndAttributeNameKList = new List<PageAttributeAffiliationGridView_Client>();

        if (this.OpenConnection() == true)
        {
            MySqlCommand cmd = new MySqlCommand(query, conn);
            MySqlDataReader dataReader = cmd.ExecuteReader();

            while (dataReader.Read())
            {
                pageInfoByURLAndAttributeNameKList.Add(new PageAttributeAffiliationGridView_Client(dataReader["RSPageAttributeAffiliationDescription"] + ""));
            }

            dataReader.Close();

            this.CloseConnection();
            Log.Debug("selectPageInfoByURLAndAttributeName method is done!");
            return pageInfoByURLAndAttributeNameKList;
        }
        else
        {
            Log.Debug("selectPageInfoByURLAndAttributeName method is done!");
            return pageInfoByURLAndAttributeNameKList;
        }
    }

    /// <summary>
    /// Search Page Button
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    protected void pageSearchButton_Click(object sender, EventArgs e)
    {
        Log.Info("pageSearchButton_Click method is called!");
        keywordSearchButton.Visible = true;
        pageURLTextBox.Visible = true;
        KeywordListBox.Visible = true;
        addKeywordDescriptionTextBox.Visible = true;
        addKeywordTextBox.Visible = true;
        addKeywordButton.Visible = true;
        updateKeywordButton.Visible = true;
        addKeywordLabel.Visible = true;
        addKeywordDescriptionLabel.Visible = true;
        PageAttributeShowCheckBox.Visible = false;
        PageAttributeAffiliationCheckBox.Visible = false;
        PageKeywordCheckBox.Visible = false;
        PageKeywordAffiliationCheckBox.Visible = false;
        GoogleCheckBox.Visible = false;
        BindKeywordListBox();

        string url = pageSearchTextBox.Text;
        if (url.Contains("'"))
        {
            url = url.Replace(@"'", @"\'");
        }
        pageGridViewList = searchPage(url);

        this.PageGridView.SelectedIndex = -1;

        if (pageGridViewList.Count != 0)
        {
            PageGridView.DataSource = pageGridViewList;
            PageGridView.DataBind();
            Label9.Visible = true;
            Label9.Text = "Total Number of Pages: " + pageGridViewList.Count;

            PageAttributeSummaryGridView.DataSource = null;
            PageAttributeSummaryGridView.DataBind();
            PageAttributeAffiliationGridView.DataSource = null;
            PageAttributeAffiliationGridView.DataBind();

            PageKeywordSummaryGridView.DataSource = null;
            PageKeywordSummaryGridView.DataBind();
            PageKeywordAffiliationGridView.DataSource = null;
            PageKeywordAffiliationGridView.DataBind();

            pageSearchTextBox.Text = "";            
        }
        else
        {
            Label9.Visible = true;
            Label9.Text = "The queried data does not exists!";
            PageGridView.DataSource = null; ;
            PageGridView.DataBind();

            PageAttributeSummaryGridView.DataSource = null;
            PageAttributeSummaryGridView.DataBind();
            PageAttributeAffiliationGridView.DataSource = null;
            PageAttributeAffiliationGridView.DataBind();

            PageKeywordSummaryGridView.DataSource = null;
            PageKeywordSummaryGridView.DataBind();
            PageKeywordAffiliationGridView.DataSource = null;
            PageKeywordAffiliationGridView.DataBind();
            pageSearchTextBox.Text = "";            
        }
        Log.Info("pageSearchButton_Click method is done!");
    }

    /// <summary>
    /// Search Page by using Page Name
    /// </summary>
    /// <param name="pageName"></param>
    /// <returns></returns>
    protected List<PageGridView_Client> searchPage(string pageName)
    {
        Log.Debug("searchPage method is called!");
        string query = "select * from RSPage where RSPageName like '" + pageName + "%' order by url";

        List<PageGridView_Client> searchPageList = new List<PageGridView_Client>();

        if (this.OpenConnection() == true)
        {
            MySqlCommand cmd = new MySqlCommand(query, conn);
            MySqlDataReader dataReader = cmd.ExecuteReader();

            while (dataReader.Read())
            {
                searchPageList.Add(new PageGridView_Client(dataReader["url"] + "", dataReader["RSPageCreateDate"] + "", 
                    dataReader["RSPageModifiedDate"] + "", dataReader["RSPageLastAccessDate"] + ""));
            }

            dataReader.Close();
            this.CloseConnection();
            Log.Debug("searchPage method is done!");
            return searchPageList;
        }
        else
        {
            Log.Debug("searchPage method is done!");
            return searchPageList;
        }
    }

    /// <summary>
    /// Bind data to keyword list box
    /// </summary>
    protected void BindKeywordListBox()
    {
        Log.Info("BindKeywordListBox method is called!");
        string query = "select RSKeywordId, RSKeywordName from RSKeyword";

        if (this.OpenConnection() == true)
        {
            MySqlCommand cmd = new MySqlCommand(query, conn);

            MySqlDataAdapter da = new MySqlDataAdapter(cmd);

            DataSet ds = new DataSet();

            try
            {
                da.Fill(ds);
            }
            catch (Exception ex)
            {
                Log.Error(ex.ToString());
            }

            KeywordListBox.DataSource = ds;
            KeywordListBox.DataTextField = "RSKeywordName";
            KeywordListBox.DataValueField = "RSKeywordId";
            KeywordListBox.DataBind();
            KeywordListBox.Items.Insert(0, new ListItem("-- Select All --", "0"));
            KeywordListBox.Items[0].Selected = true;

            this.CloseConnection();          
        }
        Log.Info("BindKeywordListBox method is done!");
    }

    /// <summary>
    /// Add keyword button
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    protected void addKeywordButton_Click(object sender, EventArgs e)
    {
        Log.Info("addKeywordButton_Click method is called!");
        string keyword = addKeywordTextBox.Text;
  
        if (existKeyword(keyword))
        {
            addKeywordLabel.Text = "The Keyword already exists!";           
        }
        else
        {
            if (addKeyword(keyword))
            {
                addKeywordLabel.Text = "Successful! Please click the Update Keyword Button!";                
            }
            else
            {
                addKeywordLabel.Text = "Failed! Please try again!";                
            }
        }
        Log.Info("addKeywordButton_Click method is done!");
    }

    /// <summary>
    /// Check the keyword exists or not
    /// </summary>
    /// <param name="keyword">keyword</param>
    /// <returns>True or False</returns>
    protected bool existKeyword(string keyword)
    {
        Log.Debug("existKeyword method is called!");
        string query = "select RSKeywordName from RSKeyword";
        string existKeyword = null;
        if (this.OpenConnection() == true)
        {
            MySqlCommand cmd = new MySqlCommand(query, conn);

            MySqlDataReader dataReader = cmd.ExecuteReader();

            while (dataReader.Read())
            {
                existKeyword = dataReader.GetString(0);
                if (existKeyword.ToLower().Equals(keyword.ToLower()))
                {
                    dataReader.Close();
                    this.CloseConnection();
                    Log.Info("existKeyword method is done!");
                    return true;
                }
            }
            dataReader.Close();
            this.CloseConnection();
            Log.Debug("existKeyword method is done!");
            return false;
        }
        else
        {
            Log.Debug("existKeyword method is done!");
            return false;
        }
    }

    /// <summary>
    /// Add keyword
    /// </summary>
    /// <param name="keyword">keyword</param>
    /// <returns>True or False</returns>
    protected bool addKeyword(string keyword)
    {
        Log.Debug("addKeyword method is called!");
        string query = @"insert into RSKeyword(RSKeywordName, RSKeywordDescription, RSKeywordCreateDate) values(@RSKeywordName, @RSKeywordDescription, Current_timestamp);";

        if (this.OpenConnection() == true)
        {
            MySqlCommand cmd = new MySqlCommand();
            keyword = addKeywordTextBox.Text;

            if (keyword == "")
            {
                this.CloseConnection();
                return false;
            }
            else
            {
                cmd.Parameters.AddWithValue(@"RSKeywordName", keyword);
                cmd.Parameters.AddWithValue(@"RSKeywordDescription", addKeywordDescriptionTextBox.Text);

                cmd.Connection = conn;
                cmd.CommandText = query;
                cmd.ExecuteNonQuery();
                cmd.Parameters.Clear();
                this.CloseConnection();
                Log.Debug("addKeyword method is done!");
                return true;
            }
        }
        else
        {
            Log.Debug("addKeyword method is done!");
            return false;
        }
    }

    /// <summary>
    /// Update keyword button
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    protected void updateKeywordButton_Click(object sender, EventArgs e)
    {
        Log.Info("updateKeywordButton_Click method is called!");
        BindKeywordListBox();
        addKeywordTextBox.Text = "";
        addKeywordLabel.Text = "Input Keyword (Necessary)";
        addKeywordDescriptionTextBox.Text = "";
        addKeywordDescriptionLabel.Text = "Input Description (Not Necessary)";

        string jarFilePath = HttpContext.Current.Request.MapPath("~/3Party/RSAPARunnable_KeywordNew.jar");

        Thread thread = new Thread(() => UpdateKeywordThread(jarFilePath));
        thread.Start();
        JobMessage.Visible = true;
        JobMessage.Text = "The Update Keyword job is running...";
        Log.Info("updateKeywordButton_Click method is done!");
    }

    /// <summary>
    /// Run the update keyword job with java code
    /// </summary>
    /// <param name="jarFilePath">path of the jar file</param>
    protected void UpdateKeywordThread(string jarFilePath)
    {
        Log.Info("UpdateKeywordThread method is called!");
        Process process = new Process();
        try
        {
            process.StartInfo.UseShellExecute = false;
            process.StartInfo.FileName = "java.exe";
            process.StartInfo.Arguments = "-jar " + '"' + jarFilePath;
            //process.StartInfo.CreateNoWindow = true;
            process.Start();
            process.WaitForExit();
            JobMessage.Visible = true;
            JobMessage.Text = "Job executes successfully!";
            Log.Info("UpdateKeywordThread method is done!");
        }
        catch (Exception ex)
        {
            Log.Error(ex.ToString());
        }
    }

    private static List<PageKeywordSummaryGridView_Client> pageKeywordSummaryGridViewList = new List<PageKeywordSummaryGridView_Client>();

    /// <summary>
    /// Search keyword button
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    protected void keywordSearchButton_Click(object sender, EventArgs e)
    {
        Log.Info("keywordSearchButton_Click method is called!");
        PageAttributeShowCheckBox.Visible = false;
        PageAttributeAffiliationCheckBox.Visible = false;
        GoogleCheckBox.Visible = false;
        PageKeywordCheckBox.Visible = true;
        PageKeywordSummaryGridView.PageIndex = 0;
        this.PageGridView.SelectedIndex = -1;
        this.PageKeywordSummaryGridView.SelectedIndex = -1;

        GridViewRow row = PageGridView.SelectedRow;

        List<int> RSKeywordId = new List<int>();
        string url = pageURLTextBox.Text;

        foreach (int i in KeywordListBox.GetSelectedIndices())
        {                        
            RSKeywordId.Add(Convert.ToInt32(KeywordListBox.Items[i].Value));
        }

        url = url.Replace(@"\", @"\\");
        if (url.Contains("&#39;"))
        {
            url = url.Replace(@"&#39;", @"\'");
        }

        pageKeywordSummaryGridViewList = selectPageInfoByKeyword(RSKeywordId, url);

        if (pageKeywordSummaryGridViewList.Count != 0)
        {
            Label1.Visible = true;
            Label1.Text = "The Keywords exist in Pages.";
            PageKeywordSummaryGridView.DataSource = pageKeywordSummaryGridViewList;
            PageKeywordSummaryGridView.DataBind();

            PageAttributeSummaryGridView.DataSource = null;
            PageAttributeSummaryGridView.DataBind();

            PageAttributeAffiliationGridView.DataSource = null;
            PageAttributeAffiliationGridView.DataBind();

            PageKeywordAffiliationGridView.DataSource = null;
            PageKeywordAffiliationGridView.DataBind();
        }
        else
        {
            Label1.Visible = true;
            Label1.Text = "The keywords do not exist in pages!";

            PageAttributeSummaryGridView.DataSource = null;
            PageAttributeSummaryGridView.DataBind();

            PageAttributeAffiliationGridView.DataSource = null;
            PageAttributeAffiliationGridView.DataBind();
        }

        AnalyticsGridView.DataSource = null;
        AnalyticsGridView.DataBind();
        pageURLTextBox.Text = "";
        Log.Info("keywordSearchButton_Click method is done!");
    }

    /// <summary>
    /// Delete the keyword button
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    protected void DeleteKeywordButton_Click(object sender, EventArgs e)
    {
        Log.Info("DeleteKeywordButton_Click method is called!");
        int RSKeywordId = 0;

        foreach (int i in KeywordListBox.GetSelectedIndices())
        {
            RSKeywordId = Convert.ToInt32(KeywordListBox.Items[i].Value);
            if (RSKeywordId == 0)
            {
                Label8.Visible = true;
                Label8.Text = "Please select at least one keywords!";

                PageKeywordSummaryGridView.DataSource = null;
                PageKeywordSummaryGridView.DataBind();

                PageKeywordAffiliationGridView.DataSource = null;
                PageKeywordAffiliationGridView.DataBind();
            }
            else
            {
                DeleteKeyword(RSKeywordId);
                Label8.Visible = true;
                Label8.Text = "The keyword " + KeywordListBox.Items[i].Text + " is deleted!";

                PageAttributeSummaryGridView.DataSource = null;
                PageAttributeSummaryGridView.DataBind();

                PageAttributeAffiliationGridView.DataSource = null;
                PageAttributeAffiliationGridView.DataBind();
            }
        }

        BindKeywordListBox();
        DeletePageKeywordAffiliationWithNull();
        Log.Info("DeleteKeywordButton_Click method is done!");
    }

    /// <summary>
    /// Delete the null data from PageKeywordAffiliation table.
    /// </summary>
    protected void DeletePageKeywordAffiliationWithNull()
    {
        Log.Debug("DeletePageKeywordAffiliationWithNull method is called!");
        string query = "delete from RSPageKeywordAffiliation where RSKeywordId is null";

        if (this.OpenConnection() == true)
        {
            MySqlCommand cmd = new MySqlCommand();

            cmd.Connection = conn;

            cmd.CommandText = query;
            cmd.ExecuteNonQuery();

            this.CloseConnection();
        }
        Log.Debug("DeletePageKeywordAffiliationWithNull method is done!");
    }

    /// <summary>
    /// Delete the keyword
    /// </summary>
    /// <param name="RSKeywordId">keywordId</param>
    protected void DeleteKeyword(int RSKeywordId)
    {
        Log.Debug("DeleteKeyword method is called!");
        string query = "delete from RSKeyword where RSKeywordId = " + RSKeywordId;

        if (this.OpenConnection() == true)
        {
            MySqlCommand cmd = new MySqlCommand();

            cmd.Connection = conn;
            cmd.CommandText = query;
            cmd.ExecuteNonQuery();
            this.CloseConnection();  
        }
        Log.Debug("DeleteKeyword method is done!");
    }

    /// <summary>
    /// Page Index Changing
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    protected void PageKeywordSummaryGridView_PageIndexChanging(object sender, GridViewPageEventArgs e)
    {
        Log.Info("PageKeywordSummaryGridView_PageIndexChanging method is called!");
        this.PageKeywordSummaryGridView.SelectedIndex = -1;
        this.PageKeywordAffiliationGridView.SelectedIndex = -1;

        PageKeywordAffiliationCheckBox.Visible = false;

        Label1.Visible = true;
        Label1.Text = "The Keywords exist in Pages.";

        PageKeywordSummaryGridView.PageIndex = e.NewPageIndex;
        PageKeywordSummaryGridView.DataSource = pageKeywordSummaryGridViewList;
        PageKeywordSummaryGridView.DataBind();

        PageKeywordAffiliationGridView.DataSource = null;
        PageKeywordAffiliationGridView.DataBind();
        Log.Info("PageKeywordSummaryGridView_PageIndexChanging method is done!");
    }

    /// <summary>
    /// Select page information by using keyword
    /// </summary>
    /// <param name="keywordId">keywordId</param>
    /// <param name="url">url</param>
    /// <returns>a list</returns>
    protected List<PageKeywordSummaryGridView_Client> selectPageInfoByKeyword(List<int> keywordId, string url)
    {
        Log.Debug("selectPageInfoByKeyword method is called!");
        string query = null;
        if (keywordId.ElementAt(0) == 0)
        {
            if (url == null || url == "")
            {
                query = @"SELECT url, RSKeywordName, count(*) as totalNumber 
                                         From RSPage a join RSPageKeywordAffiliation b on a.RSPageid = b.RSPageid join RSKeyword c on b.RSKeywordId = c.RSKeywordId 
                                         group by b.RSPageid, b.RSKeywordId order by RSKeywordName";
            }
            else
            {
                query = @"SELECT url, RSKeywordName, count(*) as totalNumber 
                                         From RSPage a join RSPageKeywordAffiliation b on a.RSPageid = b.RSPageid join RSKeyword c on b.RSKeywordId = c.RSKeywordId
                                         where url = '" + url + "'" +
                                         " group by b.RSPageid, b.RSKeywordId order by RSKeywordName";
            }
        }

        else
        {
            string strKeywordId = "(";

            for (int i = 0; i < keywordId.Count; i++)
            {
                if (i < keywordId.Count - 1)
                {
                    strKeywordId += keywordId[i] + ",";
                }
                else
                {
                    strKeywordId += keywordId[i];
                }
            }
            strKeywordId += ")";

            if (url == null || url == "")
            {
                query = @"SELECT url, RSKeywordName, count(*) as totalNumber 
                         From RSPage a join RSPageKeywordAffiliation b on a.RSPageid = b.RSPageid join RSKeyword c on b.RSKeywordId = c.RSKeywordId    
                         where b.RSKeywordId in " + strKeywordId +
                             " group by b.RSPageid, b.RSKeywordId";
            }
            else
            {
                query = @"SELECT url, RSKeywordName, count(*) as totalNumber 
                         From RSPage a join RSPageKeywordAffiliation b on a.RSPageid = b.RSPageid join RSKeyword c on b.RSKeywordId = c.RSKeywordId    
                         where url = '" + url + "' and b.RSKeywordId in " + strKeywordId +
                             " group by b.RSPageid, b.RSKeywordId";
            }
        }

        List<PageKeywordSummaryGridView_Client> list = new List<PageKeywordSummaryGridView_Client>();

        if (this.OpenConnection() == true)
        {

            MySqlCommand cmd = new MySqlCommand(query, conn);

            MySqlDataReader dataReader = cmd.ExecuteReader();

            while (dataReader.Read())
            {
                list.Add(new PageKeywordSummaryGridView_Client(dataReader["url"] + "", dataReader["RSKeywordName"] + "", dataReader["totalNumber"] + ""));
            }

            dataReader.Close();

            this.CloseConnection();
            Log.Debug("selectPageInfoByKeyword method is done!");
            return list;
        }
        else
        {
            Log.Debug("selectPageInfoByKeyword method is done!");
            return list;
        }
     }

    private static List<PageKeywordAffiliationGridView_Client> pageKeywordAffiliationGridViewList = new List<PageKeywordAffiliationGridView_Client>();

    /// <summary>
    /// Selected index changed
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    protected void PageKeywordSummaryGridView_SelectedIndexChanged(Object sender, EventArgs e)
    {
        Log.Info("PageKeywordSummaryGridView_SelectedIndexChanged method is called!");
        PageKeywordAffiliationCheckBox.Visible = true;
        PageKeywordSummaryGridView.PageIndex = 0;
        PageKeywordAffiliationGridView.PageIndex = 0;
        GridViewRow row = PageKeywordSummaryGridView.SelectedRow;

        //Label3.Visible = true;
        //Label3.Text = "The \"" + PageKeywordSummaryGridView.SelectedRow.Cells[2].Text + "\" in the \"" + row.Cells[1].Text + "\" page.";
        
        Label1.Visible = true;
        Label1.Text = "The Keywords eixst in Pages.";
 
        string url = row.Cells[1].Text;
        url = url.Replace(@"\", @"\\");
        if (url.Contains("&#39;"))
        {
            url = url.Replace(@"&#39;", @"\'");
        }
        string RSKeywordName = row.Cells[2].Text;
        pageKeywordAffiliationGridViewList = selectPageInfoByURLAndKeyword(url, RSKeywordName);

        if (pageKeywordAffiliationGridViewList.Count != 0)
        {
            PageKeywordAffiliationGridView.DataSource = pageKeywordAffiliationGridViewList;
            PageKeywordAffiliationGridView.DataBind();
        }
        else
        {
            PageKeywordAffiliationGridView.DataSource = null;
            PageKeywordAffiliationGridView.DataBind();
        }
        Log.Info("PageKeywordSummaryGridView_SelectedIndexChanged method is done!");
    }

    /// <summary>
    /// Page Index Changing
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    protected void PageKeywordAffiliationGridView_PageIndexChanging(object sender, GridViewPageEventArgs e)
    {
        Log.Info("PageKeywordAffiliationGridView_PageIndexChanging method is called!");
        GridViewRow row = PageKeywordSummaryGridView.SelectedRow;

        //Label3.Visible = true;
        //Label3.Text = "The \"" + row.Cells[2].Text + "\" in the \"" + row.Cells[1].Text + "\" page.";

        Label1.Visible = true;
        Label1.Text = "The Keywords exist in Pages.";

        PageKeywordAffiliationGridView.PageIndex = e.NewPageIndex;

        PageKeywordAffiliationGridView.DataSource = pageKeywordAffiliationGridViewList;

        PageKeywordAffiliationGridView.DataBind();
        Log.Info("PageKeywordAffiliationGridView_PageIndexChanging method is done!");
     }

    /// <summary>
    /// Select page information by using url and keyword name
    /// </summary>
    /// <param name="URL">URL</param>
    /// <param name="RSKeywordName">keyword name</param>
    /// <returns>a list of page information</returns>
    protected List<PageKeywordAffiliationGridView_Client> selectPageInfoByURLAndKeyword(string URL, string RSKeywordName)
    {
        Log.Debug("selectPageInfoByURLAndKeyword method is called!");
        string query = @"SELECT RSPageKeywordAffiliationDescription, RSPageKeywordAffiliationSector
                         From RSPage a join RSPageKeywordAffiliation b on a.RSPageid = b.RSPageid join RSKeyword c on b.RSKeywordId = c.RSKeywordId
                         where url = '" + URL + "' and RSKeywordName = '" + RSKeywordName + "' order by RSKeywordName";

        List<PageKeywordAffiliationGridView_Client> list = new List<PageKeywordAffiliationGridView_Client>();

        if (this.OpenConnection() == true)
        {
            MySqlCommand cmd = new MySqlCommand(query, conn);
            MySqlDataReader dataReader = cmd.ExecuteReader();

            while (dataReader.Read())
            {
                list.Add(new PageKeywordAffiliationGridView_Client(dataReader["RSPageKeywordAffiliationDescription"] + "", dataReader["RSPageKeywordAffiliationSector"] + ""));
            }

            dataReader.Close();

            this.CloseConnection();
            Log.Debug("selectPageInfoByURLAndKeyword method is done!");
            return list;
        }
        else
        {
            Log.Debug("selectPageInfoByURLAndKeyword method is done!");
            return list;
        }
    }

    /// <summary>
    /// Date search button
    /// </summary>
    /// <param name="sender"></param>
    /// <param name="e"></param>
    protected void DateSearchButton_Click(object sender, EventArgs e)
    {
        Log.Info("DateSearchButton_Click method is called!");
        PageAttributeShowCheckBox.Visible = false;
        PageAttributeAffiliationCheckBox.Visible = false;
        PageKeywordCheckBox.Visible = false;
        PageKeywordAffiliationCheckBox.Visible = false;
        GoogleCheckBox.Visible = false;
        PageGridView.DataSource = null;
        PageGridView.DataBind();

        string startDate = StartDateTextBox.Text;
        string endDate = EndDateTextBox.Text;
        string dateOption = null;

        if (BindDateDropDownList.SelectedValue == "0")
        {
            Label8.Visible = true;
            Label8.Text = "Please select one option from the dropdown list!";

            PageAttributeSummaryGridView.DataSource = null;
            PageAttributeSummaryGridView.DataBind();

            PageAttributeAffiliationGridView.DataSource = null;
            PageAttributeAffiliationGridView.DataBind();
        }
        else
        {
            dateOption = BindDateDropDownList.SelectedValue;
            if (!String.IsNullOrEmpty(startDate) && String.IsNullOrEmpty(endDate))
            {
                if (validDateFormat(startDate))
                {
                    showSearchDate(startDate, endDate, dateOption);
                    Label8.Visible = true;
                    Label8.Text = "Search Finished!";
                }
            }
            if (String.IsNullOrEmpty(startDate) && !String.IsNullOrEmpty(endDate))
            {
                if (validDateFormat(endDate))
                {
                    showSearchDate(startDate, endDate, dateOption);
                    Label8.Visible = true;
                    Label8.Text = "Search Finished!";
                }
            }
            if (!String.IsNullOrEmpty(startDate) && !String.IsNullOrEmpty(endDate))
            {

                if (validDateFormat(startDate) && validDateFormat(endDate))
                {
                    if (endDate.CompareTo(startDate) > 0)
                    {
                        showSearchDate(startDate, endDate, dateOption);
                        Label8.Visible = true;
                        Label8.Text = "Search Finished!";
                    }
                    else
                    {
                        Label8.Visible = true;
                        Label8.Text = "Please enter the End Date greater than the Start Date!";
                    }
                }
            }
            if (String.IsNullOrEmpty(startDate) && String.IsNullOrEmpty(endDate))
            {
                showSearchDate(startDate, endDate, dateOption);
                Label8.Visible = true;
                Label8.Text = "Search Finished!";
            }
        }
        Log.Info("DateSearchButton_Click method is done!");
    }

    /// <summary>
    /// Check the input date format
    /// </summary>
    /// <param name="dateInput">input date</param>
    /// <returns>True or False</returns>
    protected bool validDateFormat(string dateInput)
    {
        Log.Debug("validDateFormat method is called!");
        Match match = Regex.Match(dateInput, @"^(19|20)\d\d[-](0[1-9]|1[012])[-](0[1-9]|[12][0-9]|3[01])");
        if (match.Success)
        {
            Label8.Text = "";
            Log.Debug("validDateFormat method is done!");
            return true;
        }
        else
        {
            Label8.Visible = true;
            Label8.Text = "Please enter the date in \"yyyy-MM-dd\" format!";
            Log.Debug("validDateFormat method is done!");
            return false;
        }
    }

    /// <summary>
    /// Output the date
    /// </summary>
    /// <param name="startDate">start date</param>
    /// <param name="endDate">end date</param>
    /// <param name="dateOption">date option</param>
    protected void showSearchDate(string startDate, string endDate, string dateOption)
    {
        Log.Info("showSearchDate method is called!");
        pageGridViewList = searchDate(startDate, endDate, dateOption);
        this.PageGridView.SelectedIndex = -1;
        this.PageGridView.PageIndex = 0;

        if (pageGridViewList.Count != 0)
        {
            PageGridView.DataSource = pageGridViewList;
            PageGridView.DataBind();
            Label9.Visible = true;
            Label9.Text = "Total Number of Pages: " + pageGridViewList.Count.ToString();

            PageAttributeSummaryGridView.DataSource = null;
            PageAttributeSummaryGridView.DataBind();
            PageAttributeAffiliationGridView.DataSource = null;
            PageAttributeAffiliationGridView.DataBind();

            PageKeywordSummaryGridView.DataSource = null;
            PageKeywordSummaryGridView.DataBind();
            PageKeywordAffiliationGridView.DataSource = null;
            PageKeywordAffiliationGridView.DataBind();
        }
        else
        {
            Label9.Visible = true;
            Label9.Text = "The queried date does not exist!";
            PageAttributeSummaryGridView.DataSource = null;
            PageAttributeSummaryGridView.DataBind();
            PageAttributeAffiliationGridView.DataSource = null;
            PageAttributeAffiliationGridView.DataBind();

            PageKeywordSummaryGridView.DataSource = null;
            PageKeywordSummaryGridView.DataBind();
            PageKeywordAffiliationGridView.DataSource = null;
            PageKeywordAffiliationGridView.DataBind();
        }
        Log.Info("showSearchDate method is done!");
    }

    /// <summary>
    /// Search date
    /// </summary>
    /// <param name="startDate">start date</param>
    /// <param name="endDate">end date</param>
    /// <param name="dateOption">date option</param>
    /// <returns></returns>
    protected List<PageGridView_Client> searchDate(string startDate, string endDate, string dateOption)
    {
        Log.Debug("searchDate method is called!");
        string query = null;
        List<PageGridView_Client> searchDateList = new List<PageGridView_Client>();
        if (dateOption == "RSPageCreateDate")
        {
            if (!String.IsNullOrEmpty(startDate) && String.IsNullOrEmpty(endDate))
            {
                query = "select * from RSPage where RSPageCreateDate > '" + startDate + "' order by RSPageCreateDate";
            }

            if (String.IsNullOrEmpty(startDate) && !String.IsNullOrEmpty(endDate))
            {
                query = "select * from RSPage where RSPageCreateDate < '" + endDate + "' order by RSPageCreateDate";
            }

            if (!String.IsNullOrEmpty(startDate) && !String.IsNullOrEmpty(endDate))
            {
                query = "Select * from RSPage where RSPageCreateDate between '" + startDate + "' and '" + endDate + "' order by RSPageCreateDate";
            }
            if (String.IsNullOrEmpty(startDate) && String.IsNullOrEmpty(endDate))
            {
                query = "Select * from RSPage order by RSPageCreateDate";
            }

            if (this.OpenConnection() == true)
            {
                MySqlCommand cmd = new MySqlCommand(query, conn);

                MySqlDataReader dataReader = cmd.ExecuteReader();

                while (dataReader.Read())
                {
                    searchDateList.Add(new PageGridView_Client(dataReader["url"] + "", dataReader["RSPageCreateDate"] + "",
                        null, null));
                }

                dataReader.Close();

                this.CloseConnection();
                Log.Debug("searchDate method is done!");
                return searchDateList;
            }
            else
            {
                Log.Debug("searchDate method is done!");
                return searchDateList;
            }
        }
        if (dateOption == "RSPageModifiedDate")
        {
            if (!String.IsNullOrEmpty(startDate) && String.IsNullOrEmpty(endDate))
            {
                query = "select * from RSPage where RSPageModifiedDate > '" + startDate + "' order by RSPageModifiedDate";
            }

            if (String.IsNullOrEmpty(startDate) && !String.IsNullOrEmpty(endDate))
            {
                query = "select * from RSPage where RSPageModifiedDate < '" + endDate + "' order by RSPageModifiedDate";
            }

            if (!String.IsNullOrEmpty(startDate) && !String.IsNullOrEmpty(endDate))
            {
                query = "Select * from RSPage where RSPageModifiedDate between '" + startDate + "' and '" + endDate + "' order by RSPageModifiedDate";
            }
            if (String.IsNullOrEmpty(startDate) && String.IsNullOrEmpty(endDate))
            {
                query = "Select * from RSPage order by RSPageModifiedDate";
            }

            if (this.OpenConnection() == true)
            {
                MySqlCommand cmd = new MySqlCommand(query, conn);

                MySqlDataReader dataReader = cmd.ExecuteReader();

                while (dataReader.Read())
                {
                    searchDateList.Add(new PageGridView_Client(dataReader["url"] + "", null,
                        dataReader["RSPageModifiedDate"] + "", null));
                }

                dataReader.Close();

                this.CloseConnection();
                Log.Debug("searchDate method is done!");
                return searchDateList;
            }
            else
            {
                Log.Debug("searchDate method is done!");
                return searchDateList;
            }
        }

        if (dateOption == "RSPageLastAccessDate")
        {
            if (!String.IsNullOrEmpty(startDate) && String.IsNullOrEmpty(endDate))
            {
                query = "select * from RSPage where RSPageLastAccessDate > '" + startDate + "' order by RSPageLastAccessDate";
            }

            if (String.IsNullOrEmpty(startDate) && !String.IsNullOrEmpty(endDate))
            {
                query = "select * from RSPage where RSPageLastAccessDate < '" + endDate + "' order by RSPageLastAccessDate";
            }

            if (!String.IsNullOrEmpty(startDate) && !String.IsNullOrEmpty(endDate))
            {
                query = "Select * from RSPage where RSPageLastAccessDate between '" + startDate + "' and '" + endDate + "' order by RSPageLastAccessDate";
            }
            if (String.IsNullOrEmpty(startDate) && String.IsNullOrEmpty(endDate))
            {
                query = "Select * from RSPage order by RSPageLastAccessDate";
            }

            if (this.OpenConnection() == true)
            {
                MySqlCommand cmd = new MySqlCommand(query, conn);

                MySqlDataReader dataReader = cmd.ExecuteReader();

                while (dataReader.Read())
                {
                    searchDateList.Add(new PageGridView_Client(dataReader["url"] + "", null,
                        null, dataReader["RSPageLastAccessDate"] + ""));
                }

                dataReader.Close();

                this.CloseConnection();
                Log.Debug("searchDate method is done!");
                return searchDateList;
            }
            else
            {
                Log.Debug("searchDate method is done!");
                return searchDateList;
            }
        }
        return searchDateList;
    }

}


public class PageGridView_Client
{
    public PageGridView_Client(string pageURL, string RSPageCreateDate, string RSPageModifiedDate, string RSPageLastAccessDate)
    {
        _pageURL = pageURL;
        _RSPageCreateDate = RSPageCreateDate;
        _RSPageModifiedDate = RSPageModifiedDate;
        _RSPageLastAccessDate = RSPageLastAccessDate;
    }

    private string _pageURL;
    private string _RSPageCreateDate;
    private string _RSPageModifiedDate;
    private string _RSPageLastAccessDate;

    public string PageURL
    {
        get { return _pageURL; }
        set { _pageURL = value; }
    }

    public string RSPageCreateDate
    {
        get { return _RSPageCreateDate; }
        set { _RSPageCreateDate = value; }
    }

    public string RSPageModifiedDate
    {
        get { return _RSPageModifiedDate; }
        set { _RSPageModifiedDate = value; }
    }

    public string RSPageLastAccessDate
    {
        get { return _RSPageLastAccessDate; }
        set { _RSPageLastAccessDate = value; }
    }

}

public class PageAttributeSummaryGridView_Client
{
    public PageAttributeSummaryGridView_Client(string RSPageName, string RSAttributeName, string totalNumber)
    {
        _RSPageName = RSPageName;
        _RSAttributeName = RSAttributeName;
        _totalNumber = totalNumber;
    }

    private string _RSPageName;
    private string _RSAttributeName;
    private string _totalNumber;

    public string RSPageName
    {
        get { return _RSPageName; }
        set { _RSPageName = value; }
    }

    public string RSAttributeName
    {
        get { return _RSAttributeName; }
        set { _RSAttributeName = value; }
    }

    public string totalNumber
    {
        get { return _totalNumber; }
        set { _totalNumber = value; }
    }
}

public class PageAttributeAffiliationGridView_Client
{
    public PageAttributeAffiliationGridView_Client(string RSPageAttributeAffiliationDescription)
    {
        _RSPageAttributeAffiliationDescription = RSPageAttributeAffiliationDescription;
        //_RSPageAttributeAffiliationSector = RSPageAttributeAffiliationSector;
        
    }

    private string _RSPageAttributeAffiliationDescription;
    //private string _RSPageAttributeAffiliationSector;

    public string RSPageAttributeAffiliationDescription
    {
        get { return _RSPageAttributeAffiliationDescription; }
        set { _RSPageAttributeAffiliationDescription = value; }
    }

    //public string RSPageAttributeAffiliationSector
    //{
    //    get { return _RSPageAttributeAffiliationSector; }
    //    set { _RSPageAttributeAffiliationSector = value; }
    //}


}

public class PageKeywordSummaryGridView_Client
{
    public PageKeywordSummaryGridView_Client(string url, string RSKeywordName, string totalNumber)
    {
        _url = url;
        _RSKeywordName = RSKeywordName;
        _totalNumber = totalNumber;
    }

    private string _url;
    private string _RSKeywordName;
    private string _totalNumber;

    public string URL
    {
        get { return _url; }
        set { _url = value; }
    }

    public string RSKeywordName
    {
        get { return _RSKeywordName; }
        set { _RSKeywordName = value; }
    }

    public string totalNumber
    {
        get { return _totalNumber; }
        set { _totalNumber = value; }
    }
}

public class PageKeywordAffiliationGridView_Client
{
    public PageKeywordAffiliationGridView_Client(string RSPageKeywordAffiliationDescription, string RSPageKeywordAffiliationSector)
    {
        _RSPageKeywordAffiliationDescription = RSPageKeywordAffiliationDescription;
        _RSPageKeywordAffiliationSector = RSPageKeywordAffiliationSector;
        
    }

    private string _RSPageKeywordAffiliationDescription;
    private string _RSPageKeywordAffiliationSector;

    public string RSPageKeywordAffiliationDescription
    {
        get { return _RSPageKeywordAffiliationDescription; }
        set { _RSPageKeywordAffiliationDescription = value; }
    }

    public string RSPageKeywordAffiliationSector
    {
        get { return _RSPageKeywordAffiliationSector; }
        set { _RSPageKeywordAffiliationSector = value; }
    }
}

public class AnalyticsGridView_Client
{       
    public AnalyticsGridView_Client(string pagePath, string date, string pageViews, string uniquePageViews, string avgTimeOnPage)
    {
        _pagePath = pagePath;
        _date = date;
        _pageViews = pageViews;
        _uniquePageViews = uniquePageViews;
        _avgTimeOnPage = avgTimeOnPage;
    }

    private string _pagePath;
    private string _date;
    private string _pageViews;
    private string _uniquePageViews;
    private string _avgTimeOnPage;

    public string PagePath
    {
        get { return _pagePath; }
        set { _pagePath = value; }
    }

    public string Date
    {
        get { return _date; }
        set { _date = value; }
    }

    public string PageView
    {
        get { return _pageViews; }
        set { _pageViews = value; }
    }

    public string UniquePageViews
    {
        get { return _uniquePageViews; }
        set { _uniquePageViews = value; }
    }

    public string AvgTimeOnPage
    {
        get { return _avgTimeOnPage; }
        set { _avgTimeOnPage = value; }
    }
}

