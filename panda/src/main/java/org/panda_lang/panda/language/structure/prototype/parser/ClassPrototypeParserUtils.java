/*
 * Copyright (c) 2015-2017 Dzikoysk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.panda_lang.panda.language.structure.prototype.parser;

import org.panda_lang.panda.core.interpreter.lexer.pattern.TokenHollowRedactor;
import org.panda_lang.panda.core.interpreter.parser.util.Components;
import org.panda_lang.panda.core.structure.PandaScript;
import org.panda_lang.panda.framework.implementation.interpreter.parser.PandaParserException;
import org.panda_lang.panda.framework.language.interpreter.parser.ParserInfo;
import org.panda_lang.panda.framework.language.interpreter.token.Token;
import org.panda_lang.panda.framework.language.interpreter.token.TokenType;
import org.panda_lang.panda.framework.language.interpreter.token.TokenizedSource;
import org.panda_lang.panda.language.structure.overall.imports.ImportRegistry;
import org.panda_lang.panda.language.structure.prototype.structure.ClassPrototype;

public class ClassPrototypeParserUtils {

    public static void readDeclaration(ParserInfo delegatedInfo) {
        ClassPrototype classPrototype = delegatedInfo.getComponent("class-prototype");
        TokenHollowRedactor redactor = delegatedInfo.getComponent("redactor");
        TokenizedSource classDeclaration = redactor.get("class-declaration");
        Token next = classDeclaration.getToken(1);

        if (next.getType() != TokenType.KEYWORD) {
            throw new PandaParserException("Unknown element " + next);
        }

        switch (next.getTokenValue()) {
            case "extends":
                for (int i = 2; i < classDeclaration.size(); i++) {
                    Token classNameToken = classDeclaration.getToken(i);

                    if (classNameToken.getType() == TokenType.UNKNOWN) {
                        PandaScript script = delegatedInfo.getComponent(Components.SCRIPT);
                        ImportRegistry registry = script.getImportRegistry();
                        ClassPrototype extendedPrototype = registry.forClass(classNameToken.getTokenValue());

                        if (extendedPrototype == null) {
                            throw new PandaParserException("Class " + classNameToken.getTokenValue() + " not found");
                        }

                        classPrototype.getExtended().add(extendedPrototype);
                        continue;
                    }
                    else if (classNameToken.getType() == TokenType.SEPARATOR) {
                        continue;
                    }

                    break;
                }
                break;
            default:
                throw new PandaParserException("Illegal keyword " + next);
        }
    }

}
