/*
 ***** BEGIN LICENSE BLOCK *****
 * Version: EPL 2.0/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Eclipse Public
 * License Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.eclipse.org/legal/epl-v10.html
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * Copyright (C) 2002 Jan Arne Petersen <jpetersen@uni-bonn.de>
 * Copyright (C) 2002 Benoit Cerrina <b.cerrina@wanadoo.fr>
 * Copyright (C) 2002-2004 Anders Bengtsson <ndrsbngtssn@yahoo.se>
 * Copyright (C) 2004 Thomas E Enebo <enebo@acm.org>
 * 
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the EPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the EPL, the GPL or the LGPL.
 ***** END LICENSE BLOCK *****/
package org.jruby.ast;

import java.util.List;

import org.jruby.ast.visitor.NodeVisitor;
import org.jruby.lexer.LexingCommon;
import org.jruby.lexer.yacc.ISourcePosition;
import org.jruby.util.ByteList;
import org.jruby.util.StringSupport;

/** Represents an operator assignment to an element.
 * 
 * This could be for example:
 * 
 * <pre>
 * a[4] += 5
 * a[3] &&= true
 * </pre>
 */
public class OpElementAsgnNode extends Node {
    private final Node receiverNode;
    private final Node argsNode;
    private final Node valueNode;
    private final ByteList operatorName;

    public OpElementAsgnNode(ISourcePosition position, Node receiverNode, ByteList operatorName, Node argsNode, Node valueNode) {
        super(position, receiverNode.containsVariableAssignment() || argsNode != null && argsNode.containsVariableAssignment() || valueNode.containsVariableAssignment());
        
        assert receiverNode != null : "receiverNode is not null";
        assert valueNode != null : "valueNode is not null";
        
        this.receiverNode = receiverNode;
        this.argsNode = argsNode;
        this.valueNode = valueNode;
        this.operatorName = operatorName;
    }

    @Deprecated
    public OpElementAsgnNode(ISourcePosition position, Node receiverNode, String operatorName, Node argsNode, Node valueNode) {
        this(position, receiverNode, StringSupport.stringAsByteList(operatorName), argsNode, valueNode);
    }

    public NodeType getNodeType() {
        return NodeType.OPELEMENTASGNNODE;
    }
    
    /**
     * Accept for the visitor pattern.
     * @param iVisitor the visitor
     **/
    public <T> T accept(NodeVisitor<T> iVisitor) {
        return iVisitor.visitOpElementAsgnNode(this);
    }

    /**
     * Gets the argsNode.
     * @return Returns a Node
     */
    public Node getArgsNode() {
        return argsNode;
    }

    /**
     * Gets the operatorName.
     * @return Returns a String
     */
    public String getOperatorName() {
        return StringSupport.byteListAsString(operatorName);
    }

    /**
     * Gets the receiverNode.
     * @return Returns a Node
     */
    public Node getReceiverNode() {
        return receiverNode;
    }
    
    public boolean isOr() {
        return operatorName == LexingCommon.OR_OR;
    }

    public boolean isAnd() {
        return operatorName == LexingCommon.AMPERSAND_AMPERSAND;
    }

    /**
     * Gets the valueNode.
     * @return Returns a Node
     */
    public Node getValueNode() {
        return valueNode;
    }

    public List<Node> childNodes() {
        return Node.createList(receiverNode, argsNode, valueNode);
    }

    @Override
    public boolean needsDefinitionCheck() {
        return false;
    }
}
