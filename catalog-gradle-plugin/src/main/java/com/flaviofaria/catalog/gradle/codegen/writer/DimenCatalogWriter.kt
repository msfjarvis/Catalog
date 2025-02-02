/*
 * Copyright (C) 2023 Flavio Faria
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.flaviofaria.catalog.gradle.codegen.writer

import com.flaviofaria.catalog.gradle.codegen.ResourceEntry
import com.flaviofaria.catalog.gradle.codegen.ResourceType
import com.flaviofaria.catalog.gradle.codegen.toCamelCase
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ExperimentalKotlinPoetApi
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName

@OptIn(ExperimentalKotlinPoetApi::class)
class DimenCatalogWriter(
  packageName: String,
) : CatalogWriter<ResourceEntry.Dimen>(
  packageName, ResourceType.Dimen,
) {

  private val dimensionResourceMember = MemberName(
    "androidx.compose.ui.res",
    "dimensionResource",
  )

  private val composeDpClass = ClassName(
    "androidx.compose.ui.unit",
    "Dp",
  )

  override fun buildExtensionMethod(
    builder: FileSpec.Builder,
    resource: ResourceEntry.Dimen,
    contextReceiver: TypeName?,
    asComposeExtensions: Boolean,
  ): FileSpec.Builder {
    val statementArgs: List<Any>
    val statementFormat: String
    if (asComposeExtensions) {
      statementFormat = "return %M(%T.dimen.%L)"
      statementArgs = mutableListOf(dimensionResourceMember, rClass, resource.name)
    } else {
      statementFormat = "return resources.getDimension(%T.dimen.%L)"
      statementArgs = mutableListOf(rClass, resource.name)
    }
    return builder.addFunction(
      FunSpec.builder(resource.name.toCamelCase())
        .apply { resource.docs?.let(::addKdoc) }
        .apply {
          if (asComposeExtensions) {
            addAnnotation(composableClass)
            addAnnotation(readOnlyComposableClass)
          }
        }
        .addModifiers(KModifier.INLINE)
        .apply { contextReceiver?.let { contextReceivers(it) } }
        .receiver(
          if (asComposeExtensions) {
            composeReceiverClass
          } else {
            resourcesReceiverClass
          }
        )
        .returns(
          if (asComposeExtensions) {
            composeDpClass
          } else {
            Float::class.asClassName()
          }
        )
        .addStatement(statementFormat, *statementArgs.toTypedArray())
        .build()
    )
  }
}
