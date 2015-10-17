<#include "components/core.ftl">
<#include "components/panel.ftl">
<#include "components/button.ftl">
<#include "components/app_component.ftl">
<#include "components/credentials_input.ftl">
<#include "components/cancel_confirmation.ftl">
<#include "components/alternative_actions.ftl">
<#include "components/bank_selector.ftl">
<#include "components/radio_account_list.ftl">
<#include "components/authorization_term.ftl">
<#include "components/printable_terms_button.ftl">
<#include "components/image.ftl">
<#include "components/ech_inputs.ftl">
<#include "components/ech_info.ftl">
<#attempt>
<#visit ncl_module.getNode(page)>
<#recover>
  recover block
</#attempt>