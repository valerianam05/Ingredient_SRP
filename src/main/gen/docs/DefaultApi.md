# DefaultApi

All URIs are relative to *http://localhost*

| Method | HTTP request | Description |
|------------- | ------------- | -------------|
| [**dishesGet**](DefaultApi.md#dishesGet) | **GET** /dishes | Retourne la liste des plats |
| [**dishesIdIngredientsPut**](DefaultApi.md#dishesIdIngredientsPut) | **PUT** /dishes/{id}/ingredients | Modifier la liste des ingrédients associés à un plat |
| [**ingredientsGet**](DefaultApi.md#ingredientsGet) | **GET** /ingredients | Retourne la liste des ingrédients |
| [**ingredientsIdGet**](DefaultApi.md#ingredientsIdGet) | **GET** /ingredients/{id} | Retourne un ingrédient par son identifiant |
| [**ingredientsIdStockGet**](DefaultApi.md#ingredientsIdStockGet) | **GET** /ingredients/{id}/stock | Retourne la valeur de stock d&#39;un ingrédient |


<a id="dishesGet"></a>
# **dishesGet**
> List&lt;Dish&gt; dishesGet()

Retourne la liste des plats

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DefaultApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DefaultApi apiInstance = new DefaultApi(defaultClient);
    try {
      List<Dish> result = apiInstance.dishesGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#dishesGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;Dish&gt;**](Dish.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Liste des plats avec leurs ingrédients |  -  |

<a id="dishesIdIngredientsPut"></a>
# **dishesIdIngredientsPut**
> dishesIdIngredientsPut(id, ingredient)

Modifier la liste des ingrédients associés à un plat

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DefaultApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DefaultApi apiInstance = new DefaultApi(defaultClient);
    Integer id = 56; // Integer | 
    List<Ingredient> ingredient = Arrays.asList(); // List<Ingredient> | Liste des ingrédients à associer ou dissocier
    try {
      apiInstance.dishesIdIngredientsPut(id, ingredient);
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#dishesIdIngredientsPut");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Integer**|  | |
| **ingredient** | [**List&lt;Ingredient&gt;**](Ingredient.md)| Liste des ingrédients à associer ou dissocier | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Mise à jour réussie |  -  |
| **400** | Corps de requête obligatoire manquant |  -  |
| **404** | Dish.id&#x3D;{id} is not found |  -  |

<a id="ingredientsGet"></a>
# **ingredientsGet**
> List&lt;Ingredient&gt; ingredientsGet()

Retourne la liste des ingrédients

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DefaultApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DefaultApi apiInstance = new DefaultApi(defaultClient);
    try {
      List<Ingredient> result = apiInstance.ingredientsGet();
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#ingredientsGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;Ingredient&gt;**](Ingredient.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Liste des ingrédients récupérée avec succès |  -  |

<a id="ingredientsIdGet"></a>
# **ingredientsIdGet**
> Ingredient ingredientsIdGet(id)

Retourne un ingrédient par son identifiant

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DefaultApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DefaultApi apiInstance = new DefaultApi(defaultClient);
    Integer id = 56; // Integer | 
    try {
      Ingredient result = apiInstance.ingredientsIdGet(id);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#ingredientsIdGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Integer**|  | |

### Return type

[**Ingredient**](Ingredient.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, text/plain

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Objet JSON relatif à l’ingrédient |  -  |
| **404** | Ingredient.id&#x3D;{id} is not found |  -  |

<a id="ingredientsIdStockGet"></a>
# **ingredientsIdStockGet**
> IngredientsIdStockGet200Response ingredientsIdStockGet(id, at, unit)

Retourne la valeur de stock d&#39;un ingrédient

### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DefaultApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DefaultApi apiInstance = new DefaultApi(defaultClient);
    Integer id = 56; // Integer | 
    OffsetDateTime at = OffsetDateTime.now(); // OffsetDateTime | Moment voulu pour récupérer la valeur du stock
    String unit = "PCS"; // String | Unité de mesure (PCS, KG, L)
    try {
      IngredientsIdStockGet200Response result = apiInstance.ingredientsIdStockGet(id, at, unit);
      System.out.println(result);
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#ingredientsIdStockGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

| Name | Type | Description  | Notes |
|------------- | ------------- | ------------- | -------------|
| **id** | **Integer**|  | |
| **at** | **OffsetDateTime**| Moment voulu pour récupérer la valeur du stock | |
| **unit** | **String**| Unité de mesure (PCS, KG, L) | [enum: PCS, KG, L] |

### Return type

[**IngredientsIdStockGet200Response**](IngredientsIdStockGet200Response.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json, text/plain

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
| **200** | Valeur du stock récupérée |  -  |
| **400** | Paramètre obligatoire manquant |  -  |
| **404** | Ingredient.id&#x3D;{id} is not found |  -  |

